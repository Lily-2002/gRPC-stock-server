# Part1 : Socket-based Stock Trading System with Custom Thread Pool

## Introduction

The system implements a client-server architecture where clients can look up stock prices from a server that maintains a catalog of stocks.

## Objective

The primary objectives of this implementation are:

1. Design a distributed client-server application using socket communication
2. Implement a custom thread pool from scratch to handle concurrent client requests
3. Create a thread-per-request model for the server
4. Support multiple concurrent clients making requests to the server
5. Measure and evaluate the performance of the system under varying loads

## Solutions Overview/Architecture

### System Architecture

The system follows a classic client-server architecture:

```
+----------------+       Socket       +----------------+
|                |  Communication     |                |
|     Client     | ----------------> |     Server     |
|                |                    |                |
+----------------+                    +----------------+
                                            |
                                            | Uses
                                            v
                                     +----------------+
                                     |                |
                                     |  Thread Pool   |
                                     |                |
                                     +----------------+
                                            |
                                            | Manages
                                            v
                                     +----------------+
                                     |                |
                                     | Request Queue  |
                                     |                |
                                     +----------------+
```

### Components

1. **Server**: Listens for client connections, accepts them, and delegates request handling to the thread pool.
2. **Thread Pool**: Manages a fixed number of worker threads that process client requests.
3. **Request Queue**: Stores pending client requests to be processed by worker threads.
4. **Client**: Connects to the server, sends stock lookup requests, and receives responses.

## Implementation Details

### Server Implementation

The server component is implemented in `Server.py` and has the following key features:

1. **Socket Initialization**: Creates a TCP socket and binds it to a specified port.
2. **Thread Pool Creation**: Initializes a custom thread pool with a configurable number of worker threads.
3. **Connection Handling**: Accepts incoming client connections and adds them to the request queue.

### Thread Pool Implementation

The custom thread pool is implemented in `ThreadPool.py` and includes:

1. **Worker Threads**: A fixed number of threads that wait for and process client requests.
2. **Request Queue**: A synchronized queue that stores pending client requests.

The thread pool implementation follows these principles:

- Worker threads are created at initialization and remain alive throughout the server's lifetime.
- Each worker thread waits for a request to be added to the queue.
- When a request arrives, a worker thread dequeues it and processes it.
- After processing, the worker thread returns to the pool to handle more requests.

### Synchronization

To ensure thread safety, the implementation uses the following synchronization mechanisms:

1. **Mutex Lock**: Protects access to the request queue during enqueue and dequeue operations.
2. **Condition Variable**: Allows worker threads to wait efficiently when the queue is empty.

These mechanisms are implemented using Python's threading primitives:

```python
self.queue_lock = threading.Lock()  # Mutex for queue access
self.condition = threading.Condition(self.queue_lock)  # Condition variable
```

### Client Implementation

The client component is implemented in `Client.py` and includes:

1. **Socket Connection**: Establishes a TCP connection to the server.
2. **Request Construction**: Formats lookup requests as string messages.
3. **Response Handling**: Parses and displays the server's response.
4. **Multiple Client Simulation**: Can simulate multiple concurrent clients making requests.

### Protocol

The communication protocol between client and server is text-based:

1. **Request Format**: `"Lookup <stockName>"`
2. **Response Format**: A floating-point number representing the stock price, or an error code:
   - `-1`: Stock not found
   - `0`: Trading suspended (not used in Part 1)
   - `Positive number`: Current stock price

## APIs

### Server API

```python
class Server:
    def __init__(self, port=8889, thread_count=4):
        # Initialize server with port and thread count
        
    def start(self):
        # Start the server and listen for connections
        
    def handle_client(self, client_socket, addr):
        # Process client request and send response
```

### Thread Pool API

```python
class ThreadPool:
    def __init__(self, size):
        # Initialize thread pool with specified number of threads
        
    def start(self):
        # Start all worker threads
        
    def add_task(self, task, *args):
        # Add a new task to the request queue
        
    def worker(self):
        # Worker thread function that processes tasks
```

### Client API

```python
class Client:
    def __init__(self, host='localhost', port=8889):
        # Initialize client with server host and port
        
    def lookup_stock(self, stock_name):
        # Send lookup request to server and return response
        
    def run_client(self, requests_per_client=5):
        # Run a client that makes multiple requests
```

## Testing

The system includes a comprehensive testing framework:

1. **Basic Functionality Testing**: Verifies that clients can connect to the server and receive correct stock prices.
2. **Connection Testing**: Tests server availability and basic connectivity.
3. **Load Testing**: Measures system performance under varying loads (1-5 concurrent clients).
4. **Latency Measurement**: Records and analyzes response times for different client loads.

## Performance Evaluation

Performance evaluation is conducted using the `latency_test.py` script, which:

1. Runs tests with 1-5 concurrent clients
2. Measures the average latency for each client configuration
3. Generates plots showing the relationship between client count and latency
4. Saves results to CSV files for further analysis

## Known Issues and Limitations

1. **Fixed Thread Pool Size**: The thread pool size is static and cannot be adjusted dynamically.
2. **Simple Error Handling**: Error handling is basic and could be improved for production use.


## References

1. Python Socket Programming Documentation: https://docs.python.org/3/library/socket.html
2. Python Threading Documentation: https://docs.python.org/3/library/threading.html
3. Thread Pool Pattern: https://en.wikipedia.org/wiki/Thread_pool
