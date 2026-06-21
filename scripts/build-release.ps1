# Build JavaAm Release Package
# Usage: .\build-release.ps1 -Version "1.0.0"

param(
    [string]$Version = "1.0.0"
)

$ErrorActionPreference = "Stop"
$ReleaseDir = "release"
$BuildDir = "build"
$ZipName = "javaam-${Version}.zip"

Write-Host "Building JavaAm $Version Release..." -ForegroundColor Green

# Clean previous builds
if (Test-Path $BuildDir) { Remove-Item $BuildDir -Recurse -Force }
if (Test-Path $ReleaseDir) { Remove-Item $ReleaseDir -Recurse -Force }
New-Item -ItemType Directory -Path $BuildDir, $ReleaseDir | Out-Null

# 1. Build compiler JAR
Write-Host "Building compiler JAR..." -ForegroundColor Cyan
Push-Location compiler
mvn clean package -DskipTests -q
Copy-Item "target/javaam-compiler-*.jar" "../$BuildDir/javaam-compiler.jar"
Pop-Location

# 2. Copy examples
Write-Host "Copying examples..." -ForegroundColor Cyan
Copy-Item "examples" "$BuildDir/" -Recurse

# 3. Copy docs
Write-Host "Copying documentation..." -ForegroundColor Cyan
Copy-Item "README.md", "SETUP.md", "CHEATSHEET.md" "$BuildDir/"
Copy-Item "docs" "$BuildDir/" -Recurse

# 4. Create Windows batch launcher
Write-Host "Creating Windows launcher..." -ForegroundColor Cyan
$LauncherBat = @'
@echo off
REM JavaAm Launcher
REM Check if java is available
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Java not found. Please install Java 11 or later.
    echo Visit: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Run compiler
java -jar "%~dp0javaam-compiler.jar" %*
'@
$LauncherBat | Out-File "$BuildDir/javaam.bat" -Encoding ASCII

# 5. Create Unix launcher
Write-Host "Creating Unix launcher..." -ForegroundColor Cyan
$LauncherSh = @'
#!/bin/bash
# JavaAm Launcher
if ! command -v java &> /dev/null; then
    echo "Error: Java not found. Please install Java 11 or later."
    echo "Visit: https://www.oracle.com/java/technologies/downloads/"
    exit 1
fi

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
java -jar "$SCRIPT_DIR/javaam-compiler.jar" "$@"
'@
$LauncherSh | Out-File "$BuildDir/javaam" -Encoding ASCII -NoNewline
Add-Content "$BuildDir/javaam" "`n" -Encoding ASCII

# 6. Create Windows installer support files
Write-Host "Copying installer support files..." -ForegroundColor Cyan
Copy-Item "installer\javaam-installer.nsi" "$BuildDir\" -Force

# 7. Create installation instructions
Write-Host "Creating installation guide..." -ForegroundColor Cyan
$InstallGuide = @"
# JavaAm Language Release v$Version

## Quick Start

### Windows
1. Extract `javaam-${Version}.zip`
2. Add the extracted folder to your Windows PATH:
   - Open Environment Variables (Win+X → System → Advanced → Environment Variables)
   - Click "Edit the PATH environment variable"
   - Click "New" and paste the full path to the extracted folder
   - Click OK and restart your terminal
3. Run: `javaam compile examples/HelloWorld.jvm`

### macOS / Linux
1. Extract the archive
2. Make launchers executable:
   \`\`\`bash
   chmod +x javaam
   \`\`\`
3. Add to PATH:
   \`\`\`bash
   export PATH="\$PATH:/path/to/javaam"
   \`\`\`
4. Run: `./javaam compile examples/HelloWorld.jvm`

## Installation in VS Code

1. Open VS Code
2. Go to Extensions (Ctrl+Shift+X)
3. Search for "JavaAm"
4. Click Install

## Usage

Compile a JavaAm file:
\`\`\`bash
javaam compile MyFile.jvm
\`\`\`

See more examples in the \`examples/\` folder.

## Requirements

- Java 11 or later (download from https://www.oracle.com/java/technologies/downloads/)
- For development: Maven (optional)

## Support

- GitHub: https://github.com/amethystdevs/javaam
- Issues: https://github.com/amethystdevs/javaam/issues

## License

See LICENSE file
"@
$InstallGuide | Out-File "$BuildDir/INSTALL.md" -Encoding UTF8

# 7. Package as ZIP
Write-Host "Packaging release..." -ForegroundColor Cyan
Compress-Archive -Path "$BuildDir/*" -DestinationPath "$ReleaseDir/$ZipName" -Force

# 8. Create checksum
Write-Host "Creating checksum..." -ForegroundColor Cyan
$Hash = (Get-FileHash "$ReleaseDir/$ZipName" -Algorithm SHA256).Hash
$Hash | Out-File "$ReleaseDir/$ZipName.sha256" -Encoding ASCII

# 8. Build Windows installer if NSIS is installed
$InstallerExe = "$ReleaseDir\javaam-$Version-installer.exe"
if (Get-Command makensis -ErrorAction SilentlyContinue) {
    Write-Host "Building Windows installer..." -ForegroundColor Cyan
    Copy-Item "installer\javaam-installer.nsi" "$BuildDir\javaam-installer.nsi" -Force
    Push-Location $BuildDir
    makensis "javaam-installer.nsi"
    Move-Item "javaam-${Version}-installer.exe" "..\$InstallerExe" -Force
    Pop-Location
    Write-Host "Created installer: $InstallerExe" -ForegroundColor Green
} else {
    Write-Host "NSIS not installed; skipping Windows installer build." -ForegroundColor Yellow
}

# 9. Summary
Write-Host "`n✓ Release built successfully!" -ForegroundColor Green
Write-Host "Location: $ReleaseDir/$ZipName" -ForegroundColor Green
if (Test-Path $InstallerExe) { Write-Host "Windows installer: $InstallerExe" -ForegroundColor Green }
Write-Host "SHA256: $Hash" -ForegroundColor Yellow
Write-Host "`nNext steps:" -ForegroundColor Cyan
Write-Host "1. Create GitHub Release: https://github.com/amethystdevs/javaam/releases/new"
Write-Host "2. Upload: $ReleaseDir/$ZipName"
if (Test-Path $InstallerExe) { Write-Host "3. Upload: $InstallerExe" }
Write-Host "4. Upload: $ReleaseDir/$ZipName.sha256"
Write-Host "5. Publish to VS Code Marketplace: npm run publish"
