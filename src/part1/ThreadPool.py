import threading
from queue import Queue

# AI TOOL used here
# For this part, I use ChatGPT for creating the frame of the functions the I implemented the functions by myself.
class ThreadPool:
    def __init__(self, thread_count):
        self.taskQ = Queue()
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

    def work_imple(self):
        while True:
            c_socket, addr = self.taskQ.get()
            if c_socket is None:
                self.taskQ.task_done()
                break
            try:
                self.client_proxy(c_socket)
            except Exception as e:
                print(f"Error handling client: {e}")
            finally:
                self.taskQ.task_done()

    def task_adder(self, c_socket, addr):
        self.taskQ.put((c_socket, addr))

    def shut_down(self):
        for _ in self.threads:
            self.taskQ.put((None, None))
        for thread in self.threads:
            thread.join()

    def client_proxy(self, c_socket):
        request = c_socket.recv(1024).decode('utf-8')
        splitted = request.strip().split()
        call_func = splitted[0]
        name = splitted[1]
        if call_func == "Lookup":
            c_socket.send(str(self.lookup(name)).encode('utf-8'))
        else:
            c_socket.send(b"invalid")

        c_socket.close()

    def lookup(self, stock):
        if stock in self.stocks:
            return self.stocks[stock]
        else:
            return -1
