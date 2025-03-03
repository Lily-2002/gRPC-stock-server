package org.example;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.example.grpc.*;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class InteractiveStockClient {
    private final StockServiceGrpc.StockServiceBlockingStub blockingStub;

    public InteractiveStockClient(Channel channel) {
        blockingStub = StockServiceGrpc.newBlockingStub(channel);
    }

    public void lookup(String stockName) {
        LookupRequest request = LookupRequest.newBuilder()
                .setStockName(stockName)
                .build();
        try {
            LookupResponse response = blockingStub.lookup(request);
            if (response.getPrice() == -1) {
                System.out.printf("Stock %s not found%n", stockName);
            } else {
                System.out.printf("Stock: %s, Price: %.2f, Volume: %d%n",
                        stockName, response.getPrice(), response.getVolume());
            }
        } catch (Exception e) {
            System.err.println("RPC failed: " + e.getMessage());
        }
    }

    public void trade(String stockName, int quantity, TradeType type) {
        TradeRequest request = TradeRequest.newBuilder()
                .setStockName(stockName)
                .setQuantity(quantity)
                .setTradeType(type)
                .build();
        try {
            TradeResponse response = blockingStub.trade(request);
            String result;
            switch (response.getStatus()) {
                case 1:
                    result = "successful";
                    break;
                case 0:
                    result = "suspended (exceeded maximum trading volume)";
                    break;
                case -1:
                    result = "invalid stock";
                    break;
                default:
                    result = "unknown status";
            }
            System.out.printf("Trade %s %d shares of %s: %s%n",
                    type.toString(), quantity, stockName, result);
        } catch (Exception e) {
            System.err.println("RPC failed: " + e.getMessage());
        }
    }

    public void runInteractive() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Interactive Stock Trading System!");
        System.out.println("Available commands:");
        System.out.println("  lookup <stock_name>");
        System.out.println("  trade <stock_name> <quantity> <buy|sell>");
        System.out.println("  quit");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "lookup":
                        if (parts.length != 2) {
                            System.out.println("Usage: lookup <stock_name>");
                            continue;
                        }
                        lookup(parts[1]);
                        break;

                    case "trade":
                        if (parts.length != 4) {
                            System.out.println("Usage: trade <stock_name> <quantity> <buy|sell>");
                            continue;
                        }
                        try {
                            String stockName = parts[1];
                            int quantity = Integer.parseInt(parts[2]);
                            String typeStr = parts[3].toUpperCase();
                            TradeType type;
                            
                            if (typeStr.equals("BUY")) {
                                type = TradeType.BUY;
                            } else if (typeStr.equals("SELL")) {
                                type = TradeType.SELL;
                            } else {
                                System.out.println("Invalid trade type. Use 'buy' or 'sell'");
                                continue;
                            }
                            
                            trade(stockName, quantity, type);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid quantity. Please enter a number.");
                        }
                        break;

                    case "quit":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Unknown command. Available commands: lookup, trade, quit");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: InteractiveStockClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.println("Connecting to server at " + host + ":" + port);
        ManagedChannel channel = NettyChannelBuilder.forAddress(new InetSocketAddress(host, port))
                .usePlaintext()
                .build();
        try {
            InteractiveStockClient client = new InteractiveStockClient(channel);
            client.runInteractive();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
} 