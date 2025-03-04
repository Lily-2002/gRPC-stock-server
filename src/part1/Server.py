import socket
import sys
from ThreadPool import ThreadPool

def start_server(port, thread_count=4):
    # create socket
    server = socket.socket()
    server.bind(('localhost', port))
    server.listen(5)
    print(f"Server is running on port {port} with {thread_count} threads")
    thread_pool = ThreadPool(thread_count)
    #server loop
    while True:
        client_socket, addr = server.accept()
        print(f"Accepted connection from {addr}")
        thread_pool.task_adder(client_socket, addr)

#main func
if __name__ == "__main__":
    port, thread = 8889, 4  # default settings
    #we can input the port and thread_count we want for testing

    if len(sys.argv) > 1:
        port = int(sys.argv[1])
        if len(sys.argv) > 2:
            thread = int(sys.argv[2])

    start_server(port, thread)
