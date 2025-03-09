@echo off
REM Check if Java is available
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Java is not found in PATH. Please install Java and try again.
    exit /b 1
)

REM Check if the required files exist
if not exist "target\part2-1.0-SNAPSHOT.jar" (
    echo Application JAR not found. Please build the project first using 'mvn clean package'
    exit /b 1
)

if not exist "target\dependency" (
    echo Dependencies not found. Please build the project first using 'mvn clean package'
    exit /b 1
)

REM Run the client
echo Starting Stock Trading Client...
java -cp "target/part2-1.0-SNAPSHOT.jar;target/dependency/*" org.example.StockClient 