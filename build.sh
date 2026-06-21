#!/bin/bash

# JavaAm Build Script

echo "=========================================="
echo "JavaAm Compiler Build"
echo "=========================================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven 3.6+ first."
    exit 1
fi

# Navigate to compiler directory
cd compiler || exit

echo ""
echo "[1/3] Cleaning previous builds..."
mvn clean

echo ""
echo "[2/3] Compiling JavaAm Compiler..."
mvn compile

echo ""
echo "[3/3] Building JAR package..."
mvn package

echo ""
echo "=========================================="
echo "Build Complete!"
echo "=========================================="

cd ..

JAR_PATH="compiler/target/javaam-compiler-1.0.0.jar"
if [ -f "$JAR_PATH" ]; then
    echo "✓ Compiler JAR created at: $JAR_PATH"
    echo ""
    echo "To compile a JavaAm file:"
    echo "  java -jar $JAR_PATH compile <file.jvm>"
    echo ""
    echo "Example:"
    echo "  java -jar $JAR_PATH compile examples/HelloWorld.jvm"
else
    echo "✗ Build failed. Please check Maven output above."
    exit 1
fi
