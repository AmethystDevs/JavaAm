@echo off
REM JavaAm Build Script for Windows

echo ==========================================
echo JavaAm Compiler Build
echo ==========================================

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Maven is not installed. Please install Maven 3.6+ first.
    exit /b 1
)

REM Navigate to compiler directory
cd compiler || exit /b 1

echo.
echo [1/3] Cleaning previous builds...
call mvn clean

echo.
echo [2/3] Compiling JavaAm Compiler...
call mvn compile

echo.
echo [3/3] Building JAR package...
call mvn package

echo.
echo ==========================================
echo Build Complete!
echo ==========================================

cd ..

set JAR_PATH=compiler\target\javaam-compiler-1.0.0.jar
if exist "%JAR_PATH%" (
    echo checkmark Compiler JAR created at: %JAR_PATH%
    echo.
    echo To compile a JavaAm file:
    echo   java -jar %JAR_PATH% compile ^<file.jvm^>
    echo.
    echo Example:
    echo   java -jar %JAR_PATH% compile examples\HelloWorld.jvm
) else (
    echo X Build failed. Please check Maven output above.
    exit /b 1
)
