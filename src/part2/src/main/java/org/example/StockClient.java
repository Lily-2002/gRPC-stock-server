package org.example;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.example.grpc.*;

import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.net.InetSocketAddress;

public class StockClient {
    private final StockServiceGrpc.StockServiceBlockingStub blockingStub;
    private final Random random = new Random();
    private static final String[] STOCKS = {"GameStart", "RottenFishCo", "BoarCo", "MenhirCo"};

    public StockClient(Channel channel) {
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
            System.err.println("RPC failed: " + e.getClass().getName() + ": " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
            }
            e.printStackTrace();
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
            System.err.println("RPC failed: " + e.getClass().getName() + ": " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
    }

    public void runTrading() throws InterruptedException {
        while (true) {
            String stockName = STOCKS[random.nextInt(STOCKS.length)];
            // First lookup the stock
            lookup(stockName);
            
            // Then maybe trade it
            if (random.nextBoolean()) {
                int quantity = random.nextInt(10) + 1;
                TradeType type = random.nextBoolean() ? TradeType.BUY : TradeType.SELL;
                trade(stockName, quantity, type);
            }
            
            // Wait a bit before next operation
            Thread.sleep(1000 + random.nextInt(2000));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: StockClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.println("Connecting to server at " + host + ":" + port);
        ManagedChannel channel = NettyChannelBuilder.forAddress(new InetSocketAddress(host, port))
                .usePlaintext()
                .build();
        try {
            StockClient client = new StockClient(channel);
            client.runTrading();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}