import socket
import time
import sys
import matplotlib.pyplot as plt
import numpy as np
from multiprocessing import Process, Queue
import os

# this function runs a client that sends requests to server
def client_process(host, port, num_requests, result_queue, client_id):
    # store latencies in a list
    latencies = []
    
    print("Client " + str(client_id) + " starting " + str(num_requests) + " requests")
    
    # loop through requests
    for i in range(num_requests):
        # get start time
        start = time.time()
        
        try:
            # make a socket
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # connect to server
            s.connect((host, port))
            
            # send lookup request
            s.send(b"Lookup GameStart")
            # get response
            response = s.recv(1024)
            
            # calculate time it took
            end = time.time()
            latency = end - start
            latencies.append(latency)
            
            # print some progress
            if i % 20 == 0:
                print("Client", client_id, "request", i, "latency:", latency)
                
        except Exception as e:
            print("Error:", e)
        
        # always close the socket
        s.close()
    
    # calculate average latency
    avg = sum(latencies) / len(latencies)
    result_queue.put((client_id, avg, latencies))
    print("Client", client_id, "done with avg latency:", avg)

# test with multiple clients
def run_test(host, port, num_clients, requests_per_client):
    print("Testing with", num_clients, "clients making", requests_per_client, "requests each")
    
    # create a queue for results
    results = Queue()
    processes = []
    
    # start client processes
    for i in range(num_clients):
        p = Process(target=client_process, args=(host, port, requests_per_client, results, i))
        processes.append(p)
        p.start()
    
    # wait for all processes to finish
    for p in processes:
        p.join()
    
    # get results from queue
    all_results = []
    while not results.empty():
        all_results.append(results.get())
    
    # calculate overall average latency
    all_latencies = []
    for _, _, lats in all_results:
        all_latencies = all_latencies + lats
    
    avg_latency = sum(all_latencies) / len(all_latencies)
    
    print("\nResults:")
    print("Number of clients:", num_clients)
    print("Requests per client:", requests_per_client)
    print("Total requests:", num_clients * requests_per_client)
    print("Average latency:", avg_latency, "seconds")
    
    return avg_latency

# run tests with different numbers of clients
def test_with_different_clients(host, port, max_clients, requests):
    results = []
    
    # test with 1 to max_clients
    for n in range(1, max_clients + 1):
        print("\n--- Testing with", n, "clients ---")
        avg = run_test(host, port, n, requests)
        results.append((n, avg))
    
    return results

# make a plot of the results
def make_plot(results, filename):
    # extract data for plotting
    clients = []
    latencies = []
    
    for c, l in results:
        clients.append(c)
        # convert to milliseconds for better readability
        latencies.append(l * 1000)
    
    # create plot
    plt.figure(figsize=(8, 5))
    plt.plot(clients, latencies, 'o-')
    plt.title('Latency vs Number of Clients')
    plt.xlabel('Number of Clients')
    plt.ylabel('Average Latency (ms)')
    plt.grid(True)
    plt.xticks(clients)
    
    # save the plot
    plt.savefig(filename)
    print("Plot saved to", filename)
    
    # show the plot
    plt.show()

# test what happens when clients exceed thread pool size
def test_thread_pool_effect(host, port, thread_pool_size, requests):
    results = []
    
    # test with clients from 1 to double the thread pool size
    max_clients = thread_pool_size * 2
    for n in range(1, max_clients + 1):
        print("\n--- Testing with", n, "clients (thread pool:", thread_pool_size, ") ---")
        avg = run_test(host, port, n, requests)
        results.append((n, avg))
    
    # plot results
    clients = []
    latencies = []
    
    for c, l in results:
        clients.append(c)
        latencies.append(l * 1000)  # convert to ms
    
    plt.figure(figsize=(8, 5))
    plt.plot(clients, latencies, 'o-')
    
    # add line for thread pool size
    plt.axvline(x=thread_pool_size, color='r', linestyle='--', 
                label='Thread Pool Size = ' + str(thread_pool_size))
    
    plt.title('Effect of Thread Pool Size on Latency')
    plt.xlabel('Number of Clients')
    plt.ylabel('Average Latency (ms)')
    plt.grid(True)
    plt.legend()
    
    # save plot
    plt.savefig('thread_pool_effect.png')
    print("Thread pool effect plot saved to thread_pool_effect.png")
    
    plt.show()
    
    return results

# main function
if __name__ == "__main__":
    # default settings
    host = 'localhost'
    port = 8889
    max_clients = 5
    requests_per_client = 50
    thread_pool_size = 4
    
    # get command line arguments
    if len(sys.argv) > 1:
        host = sys.argv[1]
    if len(sys.argv) > 2:
        port = int(sys.argv[2])
    if len(sys.argv) > 3:
        max_clients = int(sys.argv[3])
    if len(sys.argv) > 4:
        requests_per_client = int(sys.argv[4])
    if len(sys.argv) > 5:
        thread_pool_size = int(sys.argv[5])
    
    # make results directory
    if not os.path.exists('results'):
        os.mkdir('results')
    
    # run standard tests
    print("=== Running latency tests ===")
    results = test_with_different_clients(host, port, max_clients, requests_per_client)
    make_plot(results, 'results/latency_plot.png')
    
    # save results to file
    with open('results/latency_data.csv', 'w') as f:
        f.write('clients,latency_ms\n')
        for c, l in results:
            f.write(f'{c},{l*1000}\n')
    
    # run thread pool test
    print("\n=== Testing thread pool effect ===")
    thread_results = test_thread_pool_effect(host, port, thread_pool_size, 
                                           requests_per_client)
    
    # save thread pool results
    with open('results/thread_pool_data.csv', 'w') as f:
        f.write('clients,latency_ms,thread_pool_size\n')
        for c, l in thread_results:
            f.write(f'{c},{l*1000},{thread_pool_size}\n')
    
    print("\nAll tests completed!")
