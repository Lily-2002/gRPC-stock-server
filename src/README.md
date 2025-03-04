
 # Part2
The part2 is a stock trading system based on gRPC. The code is constructed by Maven. 
The structure of the system:
```text
part2/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── StockServer.java          # gRPC server
│   │   │   ├── StockServiceImpl.java     # The implement of the server components 
│   │   │   ├── StockClient.java          # server
│   │   │   ├── InteractiveStockClient.java # interactive client
│   │   │   └── LoadTest.java             # evaluation
│   │   │   └── Stock.java   # Stock class
│   │   └── proto/
│   │       └── stock_service.proto        # Protocol Buffers 
├── pom.xml                               # Maven setting
└── run_load_test.sh                      # evaluation bash
```
## Usage:
1. construct the project with MAVEN.
```bash
mvn clean package
```
2. Start the server:
```bash
java -cp target/part2-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.StockServer 172.17.0.3 50054 GameStart=2000 RottenFishCo=1000 BoarCo=1500 MenhirCo=3000
```
3. Start the no interactive client:
```bash
java -cp target/part2-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.StockClient 172.17.0.3 50054
```
1. Start the interactive client:
```bash
java -cp target/part2-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.InteractiveStockClient 172.17.0.3 50054
```
Human Write Code:
1. StockServer.java 
2. StockServiceImpl.java
3. Stock.java
4. StockClient.java


AI assisted generation:
1. interactiveStockClient.java
2. LoadTest.java

