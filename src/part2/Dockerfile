FROM ubuntu:latest

# Install OpenJDK 17, Maven, and other necessary tools
RUN apt-get update && \
    apt-get install -y \
    openjdk-17-jdk \
    maven \
    protobuf-compiler \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the project files
COPY . .

# Pre-download Maven dependencies
RUN mvn dependency:go-offline

# Build the project
RUN mvn clean package

# Expose the gRPC port
EXPOSE 50053

# Command to run the server
CMD ["java", "-cp", "target/part2-1.0-SNAPSHOT.jar:target/dependency/*", "org.example.StockServer"]