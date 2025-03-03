package org.example;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class InteractiveStockClient {
    private final StockServiceGrpc.StockServiceBlockingStub blockingStub;
    private static final String[] AVAILABLE_STOCKS = {"GameStart", "RottenFishCo", "BoarCo", "MenhirCo"};

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
                    result = "suspended";
                    break;
                case -1:
                    result = "invalid stock";
                    break;
                default:
                    result = "unknown status";
            }
            System.out.printf("Trade %s for %s: %s%n",
                    type.toString(), stockName, result);
        } catch (Exception e) {
            System.err.println("RPC failed: " + e.getMessage());
        }
    }

    private void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("1. lookup <stockName> - Look up the price and volume of a stock");
        System.out.println("2. buy <stockName> <quantity> - Buy a specified quantity of a stock");
        System.out.println("3. sell <stockName> <quantity> - Sell a specified quantity of a stock");
        System.out.println("4. list - List all available stocks");
        System.out.println("5. help - Show this help message");
        System.out.println("6. exit - Exit the program");
        System.out.println("\nAvailable stocks: GameStart, RottenFishCo, BoarCo, MenhirCo");
    }

    private void listStocks() {
        System.out.println("\nAvailable stocks:");
        for (String stock : AVAILABLE_STOCKS) {
            lookup(stock);
        }
    }

    public void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Stock Trading System");
        printHelp();

        while (true) {
            System.out.print("\nEnter command: ");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");

            if (parts.length == 0) continue;

            try {
                switch (parts[0].toLowerCase()) {
                    case "lookup":
                        if (parts.length != 2) {
                            System.out.println("Usage: lookup <stockName>");
                            continue;
                        }
                        lookup(parts[1]);
                        break;

                    case "buy":
                        if (parts.length != 3) {
                            System.out.println("Usage: buy <stockName> <quantity>");
                            continue;
                        }
                        trade(parts[1], Integer.parseInt(parts[2]), TradeType.BUY);
                        break;

                    case "sell":
                        if (parts.length != 3) {
                            System.out.println("Usage: sell <stockName> <quantity>");
                            continue;
                        }
                        trade(parts[1], Integer.parseInt(parts[2]), TradeType.SELL);
                        break;

                    case "list":
                        listStocks();
                        break;

                    case "help":
                        printHelp();
                        break;

                    case "exit":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Quantity must be a valid number");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String target = "172.23.42.196:50053";
        System.out.println("Connecting to server at " + target);
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
        try {
            InteractiveStockClient client = new InteractiveStockClient(channel);
            client.interactiveMode();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
} 