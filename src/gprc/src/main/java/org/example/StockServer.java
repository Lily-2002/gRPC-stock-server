package org.example;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class StockServer {
    private static final Logger logger = Logger.getLogger(StockServer.class.getName());
    private final Server server;
    private final int port;
    private final String host;
    private static final int THREAD_POOL_SIZE = 10;  // 固定线程池大小

    private static class StockInfo {
        double price;
        int volume;
        final int maxVolume;

        StockInfo(double price, int maxVolume) {
            this.price = price;
            this.volume = 0;
            this.maxVolume = maxVolume;
        }
    }

    public StockServer(String host, int port, Map<String, StockInfo> initialStocks) {
        this.port = port;
        this.host = host;
        server = NettyServerBuilder.forPort(port)
                .addService(new StockService(initialStocks))
                .executor(Executors.newFixedThreadPool(THREAD_POOL_SIZE))  // 使用固定大小的线程池
                .maxConcurrentCallsPerConnection(THREAD_POOL_SIZE)  // 限制并发连接数
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + host + ":" + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server");
            try {
                StockServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("Server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class StockService extends StockServiceGrpc.StockServiceImplBase {
        private final Map<String, StockInfo> stocks;

        public StockService(Map<String, StockInfo> initialStocks) {
            this.stocks = new ConcurrentHashMap<>(initialStocks);
        }

        @Override
        public void lookup(LookupRequest request, StreamObserver<LookupResponse> responseObserver) {
            String stockName = request.getStockName();
            StockInfo info = stocks.get(stockName);
            LookupResponse response;
            if (info != null) {
                response = LookupResponse.newBuilder()
                        .setPrice(info.price)
                        .setVolume(info.volume)
                        .build();
                logger.info(String.format("Lookup %s: price=%.2f, volume=%d", stockName, info.price, info.volume));
            } else {
                response = LookupResponse.newBuilder()
                        .setPrice(-1)
                        .setVolume(0)
                        .build();
                logger.warning("Invalid stock name: " + stockName);
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void trade(TradeRequest request, StreamObserver<TradeResponse> responseObserver) {
            String stockName = request.getStockName();
            int quantity = request.getQuantity();
            TradeType type = request.getTradeType();

            StockInfo info = stocks.get(stockName);
            int status;

            if (info == null) {
                status = -1; // Invalid stock
                logger.warning("Invalid stock name: " + stockName);
            } else if (type == TradeType.SELL && quantity > info.volume) {
                status = 0; // Not enough volume to sell
                logger.warning(String.format("Not enough volume to sell %s: requested=%d, available=%d", 
                    stockName, quantity, info.volume));
            } else if (type == TradeType.BUY && info.volume + quantity > info.maxVolume) {
                status = 0; // Would exceed max volume
                logger.warning(String.format("Trade would exceed max volume for %s: current=%d, requested=%d, max=%d", 
                    stockName, info.volume, quantity, info.maxVolume));
            } else {
                if (type == TradeType.BUY) {
                    info.volume += quantity;
                } else {
                    info.volume -= quantity;
                }
                status = 1; // Success
                logger.info(String.format("%s %d shares of %s: new volume=%d", 
                    type, quantity, stockName, info.volume));
            }

            TradeResponse response = TradeResponse.newBuilder()
                    .setStatus(status)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void update(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
            String stockName = request.getStockName();
            double newPrice = request.getPrice();
            
            StockInfo info = stocks.get(stockName);
            int status;

            if (info == null) {
                status = -1; // Invalid stock
                logger.warning("Invalid stock name: " + stockName);
            } else if (newPrice <= 0) {
                status = -2; // Invalid price
                logger.warning(String.format("Invalid price for %s: %.2f", stockName, newPrice));
            } else {
                double oldPrice = info.price;
                info.price = newPrice;
                status = 1; // Success
                logger.info(String.format("Updated price of %s: %.2f -> %.2f", 
                    stockName, oldPrice, newPrice));
            }

            UpdateResponse response = UpdateResponse.newBuilder()
                    .setStatus(status)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: StockServer <host> <port> <stock1=price1> [stock2=price2 ...]");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Map<String, StockInfo> initialStocks = new ConcurrentHashMap<>();

        // Parse initial stock prices from command line arguments
        // Format: stockName=price
                    for (int i = 2; i < args.length; i++) {
                        String[] parts = args[i].split("=");
                        if (parts.length == 2) {
                            String stockName = parts[0];
                double price = Double.parseDouble(parts[1]);
                int maxVolume = 1000; // 默认最大交易量为1000
                initialStocks.put(stockName, new StockInfo(price, maxVolume));
                logger.info(String.format("Added stock %s: price=%.2f, maxVolume=%d", 
                    stockName, price, maxVolume));
            }
        }

        final StockServer server = new StockServer(host, port, initialStocks);
        server.start();
        server.blockUntilShutdown();
    }
} 