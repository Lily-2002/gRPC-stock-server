#!/bin/bash

if ! command -v java &> /dev/null; then
    echo "Java is not found in PATH. Please install Java and try again."
    exit 1
fi

if [ ! -f "target/part2-1.0-SNAPSHOT.jar" ]; then
    echo "Application JAR not found. Please build the project first using 'mvn clean package'"
    exit 1
fi

if [ ! -d "target/dependency" ]; then
    echo "Dependencies not found. Please build the project first using 'mvn clean package'"
    exit 1
fi

echo "Starting Stock Price Updater..."
java -cp "target/part2-1.0-SNAPSHOT.jar:target/dependency/*" org.example.StockPriceUpdater 172.23.42.196:50053
