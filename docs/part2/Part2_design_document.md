# Stock Trading System Design Document

## 1. System Overview

The Stock Trading System is a distributed application implementing a stock trading service using gRPC. The system provides real-time stock price lookup and trading capabilities with support for concurrent client operations.

### 1.1 Key Features
- Real-time stock price lookup
- Buy/Sell trade operations
- Price updates
- Concurrent client support
- Performance monitoring and metrics

## 2. Architecture

### 2.1 Communication Protocol
The system uses gRPC (Google Remote Procedure Call) framework for client-server communication. gRPC was chosen for:
- High performance binary serialization using Protocol Buffers
- Built-in support for streaming and bidirectional communication
- Strong typing and contract-first development
- Excellent support for concurrent operations

### 2.2 Interface Definitions

```protobuf
service StockService {
  rpc Lookup(LookupRequest) returns (LookupResponse);
  rpc Trade(TradeRequest) returns (TradeResponse);
  rpc Update(UpdateRequest) returns (UpdateResponse);
}
```

#### Key Messages:
- `LookupRequest`: Stock name
- `LookupResponse`: Current price and volume
- `TradeRequest`: Stock name, quantity, and trade type (BUY/SELL)
- `TradeResponse`: Operation status
- `UpdateRequest`: Stock name and new price
- `UpdateResponse`: Update status

### 2.3 Concurrency Model

The server implements thread safety using:
- `ConcurrentHashMap` for stock data storage
- Thread pool executor for request handling
- Atomic operations for metrics collection

```java
server = NettyServerBuilder.forPort(port)
    .addService(new StockService(initialStocks))
    .executor(Executors.newCachedThreadPool())
    .maxConcurrentCallsPerConnection(100)
    .build();
```

## 3. Implementation Details

### 3.1 Server Design
- Uses Netty server implementation for high-performance networking
- Dynamic thread pool for handling concurrent requests
- In-memory stock data storage with thread-safe operations
- Comprehensive logging for monitoring and debugging

### 3.2 Client Design
- Interactive command-line interface for manual testing
- Load testing client for performance evaluation
- Shared channel usage for efficient resource utilization
- Automatic retry and error handling mechanisms

### 3.3 Performance Testing
The system includes a comprehensive load testing framework that:
- Measures latency for different operation types
- Supports concurrent client testing (1-5 clients)
- Generates detailed performance metrics
- Provides scalability analysis

```java
// Key metrics collected
- Average latency per operation type
- Throughput (operations/second)
- Latency distribution
- Scalability factors
```

## 4. Design Choices and Rationales

### 4.1 Thread Pool Configuration
```java
Executors.newCachedThreadPool()
```
Chosen for:
- Dynamic thread creation and reuse
- Automatic thread cleanup
- Better resource utilization under varying loads

### 4.2 Data Structure Selection
```java
private final Map<String, StockInfo> stocks = new ConcurrentHashMap<>();
```
Benefits:
- Thread-safe operations without explicit locking
- High concurrency support
- Lock-free reads for better performance

### 4.3 Performance Optimization
- Shared gRPC channels for multiple clients
- Batched operations support
- Efficient memory usage through Protocol Buffers
- Minimized object creation in hot paths

## 5. Testing and Evaluation

### 5.1 Load Testing Methodology
- Separate tests for lookup and trade operations
- Concurrent client simulation
- Latency and throughput measurements
- Scalability analysis

### 5.2 Metrics Collection
```java
static class Metrics {
    private final AtomicInteger totalOperations;
    private final ConcurrentLinkedQueue<Long> latencies;
}
```
Captures:
- Operation latencies
- Total operation counts
- Success/failure rates
- System throughput

## 6. References

1. gRPC Documentation: https://grpc.io/docs/
2. Java Concurrency in Practice (Goetz et al.)
3. Netty Project: https://netty.io/
4. Protocol Buffers: https://developers.google.com/protocol-buffers

This design document provides a high-level overview of the system's architecture, implementation details, and design rationales. The system emphasizes performance, scalability, and reliability while maintaining code maintainability and testability.