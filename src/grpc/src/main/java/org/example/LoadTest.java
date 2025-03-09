package org.example;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.example.grpc.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.Random;
import io.grpc.Channel;
import java.io.*;

public class LoadTest {
    private static final int REQUESTS_PER_CLIENT = 100;  
    private static final String[] TEST_STOCKS = {"GameStart", "BoarCo"};
    
    private static class ClientProcess {
        private final String host;
        private final int port;
        private final boolean isLookup;
        private final int clientId;
        private final String resultFile;
        
        public ClientProcess(String host, int port, boolean isLookup, int clientId) {
            this.host = host;
            this.port = port;
            this.isLookup = isLookup;
            this.clientId = clientId;
            this.resultFile = String.format("client_%d_%s.txt", 
                clientId, isLookup ? "lookup" : "trade");
        }
        
        public void run() {
            List<Double> latencies = new ArrayList<>();
            Random random = new Random();
            
            // Simple sequential requests
            for (int i = 0; i < REQUESTS_PER_CLIENT; i++) {
                ManagedChannel channel = null;
                long startTime = System.nanoTime();  // Start timing before connection establishment
                
                try {
                    // Create new connection
                    channel = NettyChannelBuilder.forAddress(new InetSocketAddress(host, port))
                        .usePlaintext()
                        .build();
                    StockServiceGrpc.StockServiceBlockingStub stub = StockServiceGrpc.newBlockingStub(channel);
                    
                    String stock = TEST_STOCKS[random.nextInt(TEST_STOCKS.length)];
                    if (isLookup) {
                        LookupRequest request = LookupRequest.newBuilder()
                            .setStockName(stock)
                            .build();
                        LookupResponse response = stub.lookup(request);
                        if (response.getPrice() >= 0) {
                            long endTime = System.nanoTime();  // End timing after request completion
                            latencies.add((endTime - startTime) / 1_000_000.0);  // Convert to milliseconds
                        }
                    } else {
                        int quantity = random.nextInt(10) + 1;
                        TradeType tradeType = random.nextBoolean() ? TradeType.BUY : TradeType.SELL;
                        TradeRequest request = TradeRequest.newBuilder()
                            .setStockName(stock)
                            .setQuantity(quantity)
                            .setTradeType(tradeType)
                            .build();
                        TradeResponse response = stub.trade(request);
                        if (response.getStatus() == 1) {
                            long endTime = System.nanoTime();  // End timing after request completion
                            latencies.add((endTime - startTime) / 1_000_000.0);  // Convert to milliseconds
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Request failed: " + e.getMessage());
                } finally {
                    if (channel != null) {
                        channel.shutdown();
                    }
                }
            }
            
            // Write results to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(resultFile))) {
                for (double latency : latencies) {
                    writer.println(latency);
                }
            } catch (IOException e) {
                System.err.println("Failed to write results: " + e.getMessage());
            }
        }
    }
    
    private static double runTest(String host, int port, int numClients, boolean isLookup) {
        List<Process> processes = new ArrayList<>();
        List<String> resultFiles = new ArrayList<>();
        CountDownLatch startSignal = new CountDownLatch(1);
        
        try {
            // Start all client processes
            for (int i = 0; i < numClients; i++) {
                String resultFile = String.format("client_%d_%s.txt", 
                    i, isLookup ? "lookup" : "trade");
                resultFiles.add(resultFile);
                
                // Build command line arguments
                List<String> command = new ArrayList<>();
                command.add("java");
                command.add("-cp");
                command.add(System.getProperty("java.class.path"));
                command.add(LoadTest.class.getName());
                command.add("--client");
                command.add(host);
                command.add(String.valueOf(port));
                command.add(String.valueOf(i));
                command.add(String.valueOf(isLookup));
                
                ProcessBuilder pb = new ProcessBuilder(command);
                Process p = pb.start();
                processes.add(p);
            }
            
            // A short delay to ensure all processes are ready
            Thread.sleep(500);
            
            // Wait for all processes to complete
            for (Process p : processes) {
                p.waitFor();
            }
            
            // Collect all latency data
            List<Double> allLatencies = new ArrayList<>();
            for (String file : resultFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        allLatencies.add(Double.parseDouble(line));
                    }
                }
                new File(file).delete();  // Clean up temporary files
            }
            
            // Calculate average latency
            return allLatencies.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
                
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            return 0.0;
        } finally {
            // Ensure all processes are terminated
            for (Process p : processes) {
                p.destroyForcibly();
            }
        }
    }
    
    public static void main(String[] args) {
        // If this is a client process
        if (args.length > 0 && args[0].equals("--client")) {
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            int clientId = Integer.parseInt(args[3]);
            boolean isLookup = Boolean.parseBoolean(args[4]);
            
            ClientProcess client = new ClientProcess(host, port, isLookup, clientId);
            client.run();
            return;
        }
        
        // Main process
        if (args.length != 2) {
            System.err.println("Usage: LoadTest <host> <port>");
            System.exit(1);
        }
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        
        System.out.println("\n=== Load Test Results ===");
        System.out.println("All latencies are in milliseconds (ms)");
        System.out.println("\nClients | Lookup (ms) | Trade (ms)");
        System.out.println("---------|------------|----------");
        
        try {
            // Test with different numbers of clients
            for (int numClients = 1; numClients <= 5; numClients++) {
                // Test lookup and trade separately
                double lookupLatency = runTest(host, port, numClients, true);
                Thread.sleep(1000);  // Cool down between tests
                
                double tradeLatency = runTest(host, port, numClients, false);
                Thread.sleep(1000);  // Cool down between tests
                
               
                System.out.printf("%-8d | %-10.3f | %-9.3f%n", 
                    numClients, lookupLatency, tradeLatency);
            }
        } catch (InterruptedException e) {
            System.err.println("Test interrupted");
            Thread.currentThread().interrupt();
        }
    }
} 