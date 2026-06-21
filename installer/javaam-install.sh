#!/bin/bash
# JavaAm Unix/Linux/macOS Installer

set -e

echo ""
echo "========================================"
echo "  JavaAm Language Installer"
echo "========================================"
echo ""

# Check Java
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java not found!"
    echo ""
    echo "JavaAm requires Java 11 or later."
    echo "Install from: https://www.oracle.com/java/technologies/downloads/"
    echo ""
    echo "Or use your package manager:"
    echo "  macOS (Homebrew): brew install openjdk@11"
    echo "  Ubuntu/Debian: sudo apt-get install openjdk-11-jdk"
    echo "  Fedora: sudo dnf install java-11-openjdk"
    echo ""
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | grep -oP '(?<=version ")[^"]*')
echo "✓ Java found: $JAVA_VERSION"

# Determine installation directory
if [ "$EUID" -eq 0 ]; then
    # Running as root
    INSTALL_DIR="/opt/javaam"
else
    # Running as regular user
    INSTALL_DIR="$HOME/.local/bin"
    mkdir -p "$INSTALL_DIR"
fi

echo ""
echo "Installing to: $INSTALL_DIR"

# Create directory if needed
mkdir -p "$INSTALL_DIR"

# Copy JAR file
echo "Copying files..."
cp javaam-compiler.jar "$INSTALL_DIR/javaam-compiler.jar"
cp javaam "$INSTALL_DIR/javaam"

# Make launcher executable
chmod +x "$INSTALL_DIR/javaam"

# Add to PATH if not root
if [ "$EUID" -ne 0 ]; then
    shell_config=""
    if [ -f ~/.bashrc ]; then
        shell_config=~/.bashrc
    elif [ -f ~/.zshrc ]; then
        shell_config=~/.zshrc
    fi
    
    if [ -n "$shell_config" ]; then
        if ! grep -q "export PATH.*\.local/bin" "$shell_config"; then
            echo "export PATH=\"\$HOME/.local/bin:\$PATH\"" >> "$shell_config"
            echo "Added $INSTALL_DIR to PATH in $shell_config"
        fi
    fi
fi

# Verify installation
echo ""
echo "Verifying installation..."
if command -v javaam &> /dev/null; then
    echo "✓ Installation successful!"
    echo ""
    echo "You can now use JavaAm:"
    echo "  javaam compile MyFile.jvm"
    echo ""
    echo "Get started:"
    echo "  javaam help"
else
    echo "⚠ Installation completed, but javaam not in PATH"
    echo ""
    echo "Add this to your shell profile (~/.bashrc, ~/.zshrc, etc):"
    echo "  export PATH=\"$INSTALL_DIR:\$PATH\""
    echo ""
    echo "Then run: source ~/.bashrc"
fi

echo ""
echo "✓ JavaAm Installation Complete!"
echo ""
