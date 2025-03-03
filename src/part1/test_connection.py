import socket
import sys

def test_connection(host='localhost', port=8889):
    """Test connection to the server"""
    print(f"Testing connection to {host}:{port}...")
    
    # Create socket
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # Try to connect
    try:
        s.connect((host, port))
        print("Connection successful!")
        
        # Try to send a simple request
        s.send(b"Lookup GameStart")
        response = s.recv(1024).decode('utf-8')
        print(f"Server responded: {response}")
        
        return True
    except Exception as e:
        print(f"Connection failed: {e}")
        return False
    finally:
        s.close()

if __name__ == "__main__":
    # Default values
    host = 'localhost'
    port = 8889
    
    # Parse command line arguments
    if len(sys.argv) > 1:
        host = sys.argv[1]
    
    if len(sys.argv) > 2:
        port = int(sys.argv[2])
    
    # Test connection
    test_connection(host, port)