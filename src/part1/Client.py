import socket
import multiprocessing
import random
import time
import sys

def request_stock_price(stock_name, server_host='localhost', server_port=8889):
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    start_time = time.time()  
    
    try:
        client.connect((server_host, server_port))
        request = f"Lookup {stock_name}"
        client.send(request.encode('utf-8'))
        price = client.recv(1024).decode('utf-8')
        latency = time.time() - start_time
        print(f"Stock price for {stock_name}: {price} (latency: {latency:.4f}s)")
        
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client.close()

def run_client(num_requests=5):
    stocks = ["GameStart", "RottenFishCo", "UnknownStock"]
    for _ in range(num_requests):
        stock = random.choice(stocks)
        request_stock_price(stock)
        time.sleep(0.5)

if __name__ == "__main__":
    num_clients = 3  #default
    requests_per_client = 5
    
    if len(sys.argv) > 1:
        num_clients = int(sys.argv[1])
        if len(sys.argv) > 2:
            requests_per_client = int(sys.argv[2])
    
    print(f"starting {num_clients} clients with {requests_per_client} requests each")
    processes = []
    for i in range(num_clients):
        p = multiprocessing.Process(
            target=run_client, 
            args=(requests_per_client,)
        )
        processes.append(p)
        p.start()
    for p in processes:
        p.join()
    
    print("finished")