@echo off
REM JavaAm Windows Installer
REM This script installs JavaAm to Program Files

setlocal enabledelayedexpansion

echo.
echo ========================================
echo   JavaAm Language Installer for Windows
echo ========================================
echo.

REM Check if running as admin
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: This installer must run as Administrator.
    echo Please right-click the installer and select "Run as administrator"
    pause
    exit /b 1
)

REM Check Java
echo Checking Java installation...
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Java not found!
    echo.
    echo JavaAm requires Java 11 or later.
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    echo.
    echo After installing Java, run this installer again.
    pause
    exit /b 1
)

REM Get Java version
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| find "version"') do set JAVA_VERSION=%%i
echo ✓ Java found: !JAVA_VERSION!

REM Installation directory
set INSTALL_DIR=%ProgramFiles%\JavaAm

echo.
echo Installing to: !INSTALL_DIR!

REM Create directory
if not exist "!INSTALL_DIR!" mkdir "!INSTALL_DIR!"

REM Copy files
echo Copying files...
copy javaam-compiler.jar "!INSTALL_DIR!\" >nul
copy javaam.bat "!INSTALL_DIR!\" >nul

REM Add to PATH if not already there
echo Updating Windows PATH...
setx PATH "!INSTALL_DIR!;!PATH!" >nul 2>&1

REM Verify installation
echo.
echo Verifying installation...
where javaam >nul 2>nul
if %errorlevel% equ 0 (
    echo ✓ Installation successful!
    echo.
    echo You can now use JavaAm from any command prompt:
    echo   javaam compile MyFile.jvm
    echo.
    echo To get started, try:
    echo   javaam help
) else (
    echo.
    echo WARNING: Installation completed, but javaam not found in PATH.
    echo You may need to restart your terminal or computer.
    echo.
    echo Manual PATH setup:
    echo 1. Open Environment Variables (Win+X ^> System ^> Advanced ^> Environment Variables^)
    echo 2. Edit PATH and add: !INSTALL_DIR!
    echo 3. Restart your terminal
)

echo.
echo ✓ JavaAm Installation Complete!
echo.
pause
