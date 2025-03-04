import socket
import multiprocessing
import random
import time
import sys

def run_client(num_requests=5, server_port=8889):
    stocks = ["GameStart", "RottenFishCo", "UnknownStock"] # Unknown is added for simulate real world cases
    # send multiple requests
    for _ in range(num_requests):
        #establish a socket for each request
        client = socket.socket()
        
        client.connect(('localhost', server_port))
        #simulate the situation by randomly choosing a stock to read its price
        stock = random.choice(stocks)
        request = f"Lookup {stock}"
        client.send(request.encode('utf-8'))
        #respond "price" from Server
        respond = client.recv(1024).decode('utf-8')
        # simulate the real world situation
        print(f"Stock price for {stock}: {respond}")
        time.sleep(0.5)
        client.close()

if __name__ == "__main__":
    clients, requests = 3, 5  #default setting
    #input the num of clients and requests for testing
    if len(sys.argv) > 1:
        clients = int(sys.argv[1])
        if len(sys.argv) > 2:
            requests = int(sys.argv[2])
    
    print(f"starting {clients} clients with {requests} requests each")
    #start client processes
    processes = []
    for i in range(clients):
        p = multiprocessing.Process(
            target=run_client, 
            args=(requests, 8889)
        )
        processes.append(p)
        p.start()

    for p in processes:
        p.join()
    print("Client finished")