# Publishing JavaAm

This guide explains how to publish JavaAm releases to GitHub, npm/VS Code Marketplace, and create installers.

## Prerequisites

1. **GitHub**: Push access to https://github.com/amethystdevs/javaam
2. **VS Code Marketplace**: 
   - Account at https://marketplace.visualstudio.com/
   - Personal Access Token (PAT) with "Marketplace (Publish)" scope
3. **Tools**:
   ```bash
   npm install -g @vscode/vsce
   ```

## Release Process

### 1. Prepare Release

```bash
# Update version in package.json
# Update CHANGELOG.md with new features

# Update version in both:
# - package.json (root and javaam-vscode/)
# - compiler/pom.xml

# Update javaam-vscode/package.json
{
  "version": "1.0.1"
}

# Update compiler/pom.xml
<version>1.0.1</version>
```

### 2. Build Installers & Packages

**Windows/macOS/Linux Release ZIP:**
```bash
PowerShell -ExecutionPolicy Bypass -File scripts/build-release.ps1 -Version "1.0.1"
```

This creates:
- `release/javaam-1.0.1.zip` - Portable package
- `release/javaam-1.0.1.zip.sha256` - Checksum

### 3. Create GitHub Release

```bash
# Tag release
git tag v1.0.1
git push origin v1.0.1

# Create release on GitHub:
# https://github.com/amethystdevs/javaam/releases/new
# - Title: "JavaAm v1.0.1"
# - Description: Features from CHANGELOG.md
# - Upload: javaam-1.0.1.zip
# - Upload: javaam-1.0.1.zip.sha256
```

### 4. Publish VS Code Extension

```bash
# In javaam-vscode/ directory
cd javaam-vscode

# Package extension
vsce package

# Publish (set PAT as environment variable)
set VSCODE_PAT_TOKEN=your-token-here
vsce publish -p %VSCODE_PAT_TOKEN%

# Or use stored token
vsce publish
```

**First-time publisher setup:**
```bash
# Create publisher account
vsce create-publisher amethystdevs

# Login
vsce login amethystdevs
```

### 5. Create Windows Installer (Optional)

For a professional .exe installer, use **NSIS** (Nullsoft Scriptable Install System):

```bash
# Install NSIS from: https://nsis.sourceforge.io/Download

# Create installer
makensis installer/javaam-installer.nsi

# Output: installer/javaam-1.0.1-installer.exe
```

See [javaam-installer.nsi](installer/javaam-installer.nsi) for the script.

---

## Installation Methods for End Users

### Method 1: VS Code Extension (Recommended)
1. Open VS Code Extensions (Ctrl+Shift+X)
2. Search "JavaAm"
3. Click Install

### Method 2: Portable ZIP Release
1. Download from GitHub Releases
2. Extract anywhere
3. Add extracted folder to PATH
4. Use: `javaam compile file.jvm`

### Method 3: One-Click Installer (Windows)
1. Download `javaam-1.0.1-installer.exe`
2. Run as Administrator
3. Follow prompts

### Method 4: bash Installer (Unix/Linux/macOS)
```bash
./javaam-install.sh
```

---

## Version Numbering

Follow [Semantic Versioning](https://semver.org/):
- **MAJOR** (1.0.0 → 2.0.0): Breaking changes
- **MINOR** (1.0.0 → 1.1.0): New features
- **PATCH** (1.0.0 → 1.0.1): Bug fixes

---

## Files to Update for Each Release

1. ✅ `package.json` - root and `javaam-vscode/`
2. ✅ `compiler/pom.xml`
3. ✅ `CHANGELOG.md`
4. ✅ `javaam-vscode/CHANGELOG.md`
5. ✅ Git tag: `git tag v1.0.1`

---

## Troubleshooting

**vsce publish fails with "401":**
- PAT token expired or invalid
- Create new token: https://dev.azure.com/_usersSettings/tokens

**Extension doesn't appear in Marketplace:**
- Wait 5-10 minutes for indexing
- Check https://marketplace.visualstudio.com/items?itemName=amethystdevs.javaam

**Installer complains about Java:**
- User must install Java 11+ from https://www.oracle.com/java/technologies/downloads/
- Update installer docs to include Java link

---

## Continuous Release (Optional)

Automate releases with GitHub Actions:

```yaml
# .github/workflows/release.yml
name: Release

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run build
      - run: npm run package
      - uses: softprops/action-gh-release@v1
        with:
          files: release/*
```

Then just tag: `git tag v1.0.1 && git push origin --tags` and GitHub Actions handles the rest!
