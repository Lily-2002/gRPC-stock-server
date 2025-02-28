import socket
import sys
from ThreadPool import ThreadPool

def start_server(port, thread_count=4):
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(('localhost', port))
    server.listen(5)
    print(f"Server is running on port {port} with {thread_count} threads...")
    thread_pool = ThreadPool(thread_count=thread_count)

    try:
        while True:
            client_socket, addr = server.accept()
            print(f"Accepted connection from {addr}")
            thread_pool.task_adder(client_socket, addr)
    except KeyboardInterrupt:
        print("\nShutting down server...")
    finally:
        server.close()
        thread_pool.shut_down()

if __name__ == "__main__":
    port, thread = 8889, 4

    #we can input the port and thread_count we want for testing
    if len(sys.argv) > 1:
        port = int(sys.argv[1])
        if len(sys.argv) > 2:
            threads = int(sys.argv[2])
    start_server(port, threads)
