package org.example;

import io.grpc.stub.StreamObserver;
import org.example.grpc.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockServiceImpl extends StockServiceGrpc.StockServiceImplBase {
    private final Map<String, Stock> stockCatalog;

    public StockServiceImpl(Map<String, Integer> maxVolumes) {
        stockCatalog = new ConcurrentHashMap<>();
        // Initialize the stock catalog with the four required stocks and their max volumes
        stockCatalog.put("GameStart", new Stock("GameStart", 100.0, maxVolumes.getOrDefault("GameStart", 1000)));
        stockCatalog.put("RottenFishCo", new Stock("RottenFishCo", 50.0, maxVolumes.getOrDefault("RottenFishCo", 1000)));
        stockCatalog.put("BoarCo", new Stock("BoarCo", 75.0, maxVolumes.getOrDefault("BoarCo", 1000)));
        stockCatalog.put("MenhirCo", new Stock("MenhirCo", 120.0, maxVolumes.getOrDefault("MenhirCo", 1000)));
    }

    @Override
    public void lookup(LookupRequest request, StreamObserver<LookupResponse> responseObserver) {
        Stock stock = stockCatalog.get(request.getStockName());
        LookupResponse.Builder response = LookupResponse.newBuilder();
        
        if (stock == null) {
            response.setPrice(-1).setVolume(0);
        } else {
            response.setPrice(stock.getPrice()).setVolume(stock.getVolume());
        }
        
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void trade(TradeRequest request, StreamObserver<TradeResponse> responseObserver) {
        Stock stock = stockCatalog.get(request.getStockName());
        TradeResponse.Builder response = TradeResponse.newBuilder();
        
        if (stock == null) {
            response.setStatus(-1);
        } else {
            int quantity = request.getQuantity();
            if (request.getTradeType() == TradeType.SELL) {
                quantity = -quantity;
            }
            
            int result = stock.trade(quantity);
            response.setStatus(result);
        }
        
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        Stock stock = stockCatalog.get(request.getStockName());
        UpdateResponse.Builder response = UpdateResponse.newBuilder();
        
        if (stock == null) {
            response.setStatus(-1);
        } else if (request.getPrice() < 0) {
            response.setStatus(-2);
        } else {
            boolean success = stock.updatePrice(request.getPrice());
            response.setStatus(success ? 1 : -2);
        }
        
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}

class StockInfo {
    private double price;
    private int volume;
    private final int maxVolume;  // 添加最大交易量限制

    public StockInfo(double price, int maxVolume) {
        this.price = price;
        this.volume = 0;
        this.maxVolume = maxVolume;
    }

    public synchronized boolean canTrade(int quantity) {
        return volume + quantity <= maxVolume;
    }
} 