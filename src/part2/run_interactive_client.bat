@echo off
REM Run the interactive stock trading client

REM Check if Java is available
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Java is not found in PATH
    exit /b 1
)

REM Check if the jar file exists
if not exist target\part2-1.0-SNAPSHOT.jar (
    echo JAR file not found. Please build the project first.
    exit /b 1
)

REM Check if dependencies exist
if not exist target\dependency (
    echo Dependencies not found. Please build the project first.
    exit /b 1
)

REM Run the interactive client
java -cp "target/part2-1.0-SNAPSHOT.jar;target/dependency/*" org.example.InteractiveStockClient %* 