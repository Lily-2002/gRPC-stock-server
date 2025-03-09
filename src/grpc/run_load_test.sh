#!/bin/bash

# Start the server
java -cp target/part2-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.StockServer 0.0.0.0 50054 GameStart=2000 BoarCo=1500 &
SERVER_PID=$!

# Wait for server to start
sleep 5

# Run load test
java -cp target/part2-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.LoadTest 172.23.42.196 50054

# Kill the server
kill $SERVER_PID 