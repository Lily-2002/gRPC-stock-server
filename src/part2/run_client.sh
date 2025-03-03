#!/bin/bash

if ! command -v java &> /dev/null; then
    echo "Java is not found in PATH"
    exit 1
fi

if [ ! -f "target/part2-1.0-SNAPSHOT.jar" ]; then
    echo "JAR file not found. Please build the project first."
    exit 1
fi

if [ ! -d "target/dependency" ]; then
    echo "Dependencies not found. Please build the project first."
    exit 1
fi

echo "Starting Interactive Stock Client..."
java -cp "target/part2-1.0-SNAPSHOT.jar:target/dependency/*" org.example.InteractiveStockClient "$@"
