package org.example;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.example.grpc.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import io.grpc.Channel;
import org.example.grpc.StockServiceGrpc;
import org.example.grpc.StockServiceProto;
import org.example.grpc.LookupRequest;
import org.example.grpc.LookupResponse;
import org.example.grpc.TradeRequest;
import org.example.grpc.TradeResponse;
import org.example.grpc.TradeType;

public class LoadTest {
    private static final int MAX_TEST_DURATION_SECONDS = 60;
    private static final int OPERATIONS_PER_CLIENT = 1000;
    private static final String[] TEST_STOCKS = {"GameStart", "BoarCo"};
    
    static class Metrics {
        private final AtomicInteger totalOperations = new AtomicInteger(0);
        private final ConcurrentLinkedQueue<Long> lookupLatencies = new ConcurrentLinkedQueue<>();
        private final ConcurrentLinkedQueue<Long> tradeLatencies = new ConcurrentLinkedQueue<>();
        
        public void recordLookupLatency(long latencyMicros) {
            lookupLatencies.add(latencyMicros);
            totalOperations.incrementAndGet();
        }
        
        public void recordTradeLatency(long latencyMicros) {
            tradeLatencies.add(latencyMicros);
            totalOperations.incrementAndGet();
        }
        
        public double getAverageLookupLatency() {
            return lookupLatencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
        }
        
        public double getAverageTradeLatency() {
            return tradeLatencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
        }
        
        public int getTotalOperations() {
            return totalOperations.get();
        }
    }
    
    static class TestClient implements Runnable {
        private final Random random;
        private final boolean lookupOnly;
        private final Metrics metrics;
        private final Channel channel;
        private final StockServiceGrpc.StockServiceBlockingStub stub;
        
        public TestClient(String host, int port, boolean lookupOnly, Metrics metrics) {
            this.random = new Random();
            this.lookupOnly = lookupOnly;
            this.metrics = metrics;
            this.channel = NettyChannelBuilder.forAddress(new InetSocketAddress(host, port))
                .usePlaintext()
                .build();
            this.stub = StockServiceGrpc.newBlockingStub(channel);
        }
        
        private void doLookup() {
            String stock = TEST_STOCKS[random.nextInt(TEST_STOCKS.length)];
            long startTime = System.nanoTime();
            try {
                LookupRequest request = LookupRequest.newBuilder()
                    .setStockName(stock)
                    .build();
                stub.lookup(request);
                long latencyMicros = (System.nanoTime() - startTime) / 1000;
                metrics.recordLookupLatency(latencyMicros);
            } catch (Exception e) {
                System.err.println("Lookup failed: " + e.getMessage());
            }
        }
        
        private void doTrade() {
            String stock = TEST_STOCKS[random.nextInt(TEST_STOCKS.length)];
            int quantity = random.nextInt(100) + 1;
            TradeType tradeType = random.nextBoolean() ? TradeType.BUY : TradeType.SELL;
            
            long startTime = System.nanoTime();
            try {
                TradeRequest request = TradeRequest.newBuilder()
                    .setStockName(stock)
                    .setQuantity(quantity)
                    .setTradeType(tradeType)
                    .build();
                stub.trade(request);
                long latencyMicros = (System.nanoTime() - startTime) / 1000;
                metrics.recordTradeLatency(latencyMicros);
            } catch (Exception e) {
                System.err.println("Trade failed: " + e.getMessage());
            }
        }
        
        @Override
        public void run() {
            int operationsCompleted = 0;
            long startTime = System.currentTimeMillis();
            
            while (operationsCompleted < OPERATIONS_PER_CLIENT &&
                   (System.currentTimeMillis() - startTime) < MAX_TEST_DURATION_SECONDS * 1000) {
                if (lookupOnly || random.nextBoolean()) {
                    doLookup();
                } else {
                    doTrade();
                }
                operationsCompleted++;
                
                try {
                    Thread.sleep(10); // Add small delay between operations
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            if (channel instanceof ManagedChannel) {
                ((ManagedChannel) channel).shutdown();
            }
        }
    }
    
    private static void runLoadTest(String host, int port, int numClients, boolean lookupOnly) {
        System.out.printf("Starting load test with %d clients (%s operations)%n",
            numClients, lookupOnly ? "lookup-only" : "mixed");
        
        ExecutorService executor = Executors.newFixedThreadPool(numClients);
        Metrics metrics = new Metrics();
        CountDownLatch latch = new CountDownLatch(numClients);
        
        for (int i = 0; i < numClients; i++) {
            executor.submit(() -> {
                try {
                    new TestClient(host, port, lookupOnly, metrics).run();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
            System.out.printf("Test completed with %d total operations%n", metrics.getTotalOperations());
            if (!lookupOnly) {
                System.out.printf("Average trade latency: %.2f microseconds%n", metrics.getAverageTradeLatency());
            }
            System.out.printf("Average lookup latency: %.2f microseconds%n", metrics.getAverageLookupLatency());
        } catch (InterruptedException e) {
            System.err.println("Test interrupted");
        } finally {
            executor.shutdown();
        }
    }
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: LoadTest <host> <port>");
            System.exit(1);
        }
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        
        // Run tests with different numbers of clients
        for (int numClients = 1; numClients <= 5; numClients++) {
            System.out.println("\n=== Testing with " + numClients + " clients ===");
            
            // Test lookup-only operations
            runLoadTest(host, port, numClients, true);
            
            // Test mixed operations
            runLoadTest(host, port, numClients, false);
        }
    }
} 