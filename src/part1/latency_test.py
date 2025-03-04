import random
import socket
import time
import sys
import matplotlib.pyplot as plt
import numpy as np
from multiprocessing import Process, Queue
import os

# this function runs a client
def client_process(host, port, num_requests, result_queue, client_id):
    latencies = []
    print("Client " + str(client_id) + " starting " + str(num_requests) + " requests")
    
    for i in range(num_requests):
        # get start time
        start = time.time()
        try:
            s = socket.socket()
            # connect
            s.connect((host, port))
            stocks = ["GameStart", "RottenFishCo", "UnknownStock"]
            stock = random.choice(stocks)
            request = f"Lookup {stock}"
            s.send(request.encode('utf-8'))
            end = time.time()
            latency = end - start
            latencies.append(latency)
                
        except Exception as e:
            print("Error:", e)
        finally:
            s.close()
    
    # calculate average latency
    avg = sum(latencies) / len(latencies)
    result_queue.put((client_id, avg, latencies))
    print("Client", client_id, "avg latency:", avg * 1000, "ms")

# test with multiple clients and have test results
def run_test(host, port, clients_count, request_num):
    print("Testing with", clients_count, "clients making", request_num, "requests each")
    results = Queue()
    processes = []
    for i in range(clients_count):
        p = Process(target=client_process, args=(host, port, request_num, results, i))
        processes.append(p)
        p.start()
    # wait for all processes to finish
    for p in processes:
        p.join()
    
    #results
    res = []
    while not results.empty():
        res.append(results.get())
    
    #caculate the avg latency
    all_latencies = []
    for _, _, curr in res:
        all_latencies = all_latencies + curr
    avg_latency = sum(all_latencies) / len(all_latencies)
    
    #print results
    print("Average latency:", avg_latency * 1000, "ms")
    return avg_latency

# run tests with different numbers of clients
def runnner(host, port, max_clients, requests):
    results = []
    # test with 1 to 5
    for n in range(1, max_clients + 1):
        print("\n--- Testing with", n, "clients ---")
        avg = run_test(host, port, n, requests)
        results.append((n, avg))
    return results

# make a plot of the results
def ploter(results, filename):
    # extract data for plotting
    clients = []
    latencies = []
    for c, l in results:
        clients.append(c)
        # convert to milliseconds
        latencies.append(l * 1000)
    
    #I used ChatGPT here
    plt.figure(figsize=(8, 5))
    plt.plot(clients, latencies, 'o-')
    plt.title('Latency vs Number of Clients')
    plt.xlabel('Number of Clients')
    plt.ylabel('Average Latency (ms)')
    plt.grid(True)
    plt.xticks(clients)
    plt.savefig(filename)

# main function
if __name__ == "__main__":
    # default settings
    host = 'localhost'
    port = 8889
    max_clients = 5
    requests_per_client = 1000
    thread_pool_size = 4
    #create dir
    if not os.path.exists('results'):
        os.mkdir('results')
    print("######### Running latency tests #########")
    results = runnner(host, port, max_clients, requests_per_client)
    ploter(results, 'results/latency_plot.png')
    # save results to file
    with open('results/latency_data.csv', 'w') as f:
        f.write('clients,latency_ms\n')
        for c, l in results:
            f.write(f'{c},{l * 1000}\n')
    print("\nEvaluation completed!")