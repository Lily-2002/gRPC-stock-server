# Part 1: Socket-based Stock Server with Custom Thread Pool

This part implements a socket-based client-server application for stock price lookups using a custom thread pool implementation.

## Overview

The implementation consists of three main components:
- `Server.py`: A socket server with a custom thread pool implementation
- `Client.py`: A client that sends stock lookup requests
- `ThreadPool.py`: A custom thread pool implementation
- `latency_test.py`: A performance testing script

## Requirements

- Python 3.6+
- matplotlib (for latency test)

## How to Run

### 1. Start the Server

```bash
python Server.py [port] [thread_count]
```

Parameters:
- `port` (optional): Port number to listen on (default: 8889)
- `thread_count` (optional): Number of threads in the thread pool (default: 4)

Example:
```bash
python Server.py 8889 4
```

### 2. Run the Client

```bash
python Client.py [host] [port] [client_count] [requests_per_client]
```

Parameters:
- `host` (optional): Server hostname (default: localhost)
- `port` (optional): Server port (default: 8889)
- `client_count` (optional): Number of clients to simulate (default: 3)
- `requests_per_client` (optional): Number of requests per client (default: 5)

Example:
```bash
python Client.py localhost 8889 3 5
```

### 3. Run Performance Tests

```bash
python latency_test.py
```

This will:
- Run tests with 1-5 clients
- Generate a latency plot in `results/latency_plot.png`
- Save latency data in `results/latency_data.csv`

## Features

- Custom thread pool implementation with a request queue
- Thread-per-request model
- Support for multiple concurrent clients
- Stock lookup functionality for "GameStart" and "RottenFishCo" stocks
- Performance measurement capabilities

## Implementation Details

- The server uses a custom thread pool to handle client requests
- Each client request is processed by an available thread from the pool
- The thread pool manages a queue of pending requests
- Synchronization is implemented using threading primitives
- The client can simulate multiple concurrent users

## Sample Output

Server output shows accepted connections:
```
Server is running on port 8889 with 4 threads
Accepted connection from ('127.0.0.1', 61329)
Accepted connection from ('127.0.0.1', 61330)
...
```

Client output shows stock prices:
```
starting 3 clients with 5 requests each
Stock price for UnknownStock: -1
Stock price for GameStart: 15.99
Stock price for RottenFishCo: 10.5
...
Client finished
```