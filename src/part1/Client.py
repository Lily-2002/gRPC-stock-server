import socket
import multiprocessing
import random
import time
import sys

def find_price(stock_name, server_host='localhost', server_port=8889):
    #connect
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    start = time.time()  
    try:
        client.connect((server_host, server_port))
        request = f"Lookup {stock_name}" 
        client.send(request.encode('utf-8'))
        price = client.recv(1024).decode('utf-8')
        latency = time.time() - start
        print(f"Stock price for {stock_name}: {price} (latency: {latency:.4f}s)")
        
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client.close()

def run_client(num_requests=5):
    # list of stocks we can look up
    stocks = ["GameStart", "RottenFishCo", "UnknownStock"]
    
    # send multiple requests
    for _ in range(num_requests):
        #simulate the situation by randomly choosing a stock to read its price
        stock = random.choice(stocks)
        find_price(stock)
        # wait a bit between requests
        time.sleep(0.5)

if __name__ == "__main__":
    clients, requests = 3, 5  #default setting
    
    #we can input the num of clients and requests each client for testing
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
            args=(requests,)
        )
        processes.append(p)
        p.start()

    for p in processes:
        p.join()
    
    print("finished")