import threading
from queue import Queue

#ChatGPT is used for framing the whole class. However, the functions are implemented by my own.
class ThreadPool:
    def __init__(self, thread_count):
        #blocking queue for tasks
        self.blockingQ = Queue() 
        self.threads = []
        self.stocks = {
            "GameStart": 15.99,
            "RottenFishCo": 10.50
        }
        
        for i in range(thread_count):
            thread = threading.Thread(target=self.work_imple)
            thread.daemon = True  
            thread.start()
            self.threads.append(thread)

    #what each worker thread does
    def work_imple(self):
        while True:
            # get a task from the queue (blocked until a task is available)
            c_socket, addr = self.blockingQ.get()
            if c_socket is None:
                self.blockingQ.task_done()
                break
            # handle the client request
            self.client_proxy(c_socket)
            self.blockingQ.task_done()

    # add a new task to the queue
    def task_adder(self, c_socket, addr):
        self.blockingQ.put((c_socket, addr))

    # shut down the thread pool
    def shut_down(self):
        # send None to each thread to exit
        for _ in self.threads:
            self.blockingQ.put((None, None))
        # wait for all threads to finish
        for thread in self.threads:
            thread.join()


    def client_proxy(self, c_socket):
        request = c_socket.recv(1024).decode('utf-8')
        # split the request into 2 parts
        splitted = request.strip().split()
        call_func = splitted[0]
        name = splitted[1]
        if call_func == "Lookup":
            # look up the stock price and send back
            c_socket.send(str(self.lookup(name)).encode('utf-8'))
        else:
            c_socket.send(b"invalid request")

        # close the socket
        c_socket.close()

    # look up the corresponding stock price
    def lookup(self, stock):
        if stock in self.stocks:
            return self.stocks[stock]
        else:
            return -1
