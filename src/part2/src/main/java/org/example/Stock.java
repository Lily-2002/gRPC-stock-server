package org.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class Stock {
    private final String name;
    private final AtomicReference<Double> price;
    private final AtomicInteger volume;
    private final int maxVolume;
    private final ReentrantLock tradeLock;
    private volatile boolean suspended;

    public Stock(String name, double initialPrice, int maxVolume) {
        this.name = name;
        this.price = new AtomicReference<>(initialPrice);
        this.volume = new AtomicInteger(0);
        this.maxVolume = maxVolume;
        this.tradeLock = new ReentrantLock();
        this.suspended = false;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price.get();
    }

    public int getVolume() {
        return volume.get();
    }

    public boolean updatePrice(double newPrice) {
        if (newPrice < 0) {
            return false;
        }
        price.set(newPrice);
        return true;
    }

    public int trade(int quantity) {
        tradeLock.lock();
        try {
            if (suspended) {
                return 0;
            }

            int newVolume = volume.get() + quantity;
            if (newVolume > maxVolume) {
                suspended = true;
                return 0;
            }

            volume.set(newVolume);
            return 1;
        } finally {
            tradeLock.unlock();
        }
    }

    public boolean isSuspended() {
        return suspended;
    }
} 