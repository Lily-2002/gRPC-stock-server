

### 1. How does the latency of Lookup compare across part 1 and part 2? Is one more efficient than the other?

Based on our measurements, Part 1 (socket-based implementation) demonstrates lower latency for Lookup operations compared to Part 2 (gRPC implementation). This efficiency difference can be attributed to several factors:

- **Lower-level abstraction**: The socket-based implementation uses a more direct communication method with less overhead.
- **Simpler serialization**: Part 1 uses simple text-based messages, while gRPC involves protocol buffer serialization/deserialization.
- **Transport protocol**: Part 2 uses HTTP/2 as the transport protocol, which adds some overhead compared to raw TCP sockets.

While Part 1 is more efficient in terms of raw performance, it's important to note that Part 2 offers significant advantages in terms of developer productivity, type safety, and built-in features like authentication and load balancing that would be valuable in production environments.

### 2. How does the latency change as the number of clients (load) is varied? Does a load increase impact response time?

From our measurements for Part 1, we observe a clear pattern:

- With 1-3 clients, latency increases gradually (0.131ms → 0.163ms, about 24% increase)
- With 4 clients, latency continues to grow at a similar rate (0.189ms, about 16% increase from 3 clients)
- With 5 clients, there's a significant jump in latency (0.332ms, about 76% increase from 4 clients)

This pattern indicates that:
1. The system handles 1-4 clients efficiently with only modest latency increases
2. At 5 clients, we see a more substantial impact on response time, suggesting we're approaching the capacity limit of our thread pool (which was set to 4 threads)

The data confirms that load increases do impact response time, with a non-linear relationship becoming apparent as we approach system capacity.

### 3. How does the latency of lookup compare to trade?

In Part 1, we only implemented the Lookup operation, so we cannot directly compare Lookup and Trade latencies. However, for Part 2, we would expect Trade operations to have higher latency than Lookup operations due to:

1. **Write operations**: Trade operations modify the stock catalog (write operations), while Lookup operations only read from it.
2. **Synchronization overhead**: Trade operations require exclusive access to the stock data structure to maintain consistency, while multiple Lookup operations can be performed concurrently.

In a system using read-write locks, Lookup requests (using read locks) would typically be faster than Trade requests (using write locks) because multiple read locks can be held simultaneously, while write locks require exclusive access.

### 4. In part 1, what happens when the number of clients is larger the size of the static thread pool? Does the response time increase due to request waiting?

Our thread pool in Part 1 was configured with 4 worker threads. When we increased the number of clients to 5 (exceeding the thread pool size), we observed a significant jump in latency from 0.189ms to 0.332ms (a 76% increase).

This substantial increase in response time occurs because:

1. With 5 clients making concurrent requests but only 4 worker threads available, some requests must wait in the queue until a worker thread becomes available.
2. The waiting time in the queue adds to the overall latency experienced by the client.
3. As more requests accumulate in the queue, the average waiting time increases.

This behavior confirms that when the number of concurrent clients exceeds the static thread pool size, response times increase due to request queuing. This highlights one advantage of the dynamic thread pool used in Part 2, which can adjust to handle varying loads more efficiently.

## Conclusion

Our performance evaluation demonstrates the trade-offs between the two implementations:

1. **Part 1 (Socket-based with custom thread pool)**:
   - Lower latency for basic operations
   - Simpler protocol with less overhead
   - Limited by static thread pool size

2. **Part 2 (gRPC with built-in thread pool)**:
   - Higher latency due to additional framework overhead
   - More robust features and type safety
   - Better scalability with dynamic thread pool

The choice between these approaches would depend on specific application requirements, balancing raw performance against developer productivity and advanced features.
