# JavaAm Quick Reference

## For Users

### Installation

**VS Code Extension (Easiest):**
```bash
# In VS Code Extensions (Ctrl+Shift+X)
# Search: JavaAm
# Click Install
```

**Command Line (Linux/macOS):**
```bash
./installer/javaam-install.sh
javaam compile MyFile.jvm
```

**Command Line (Windows):**
```bash
javaam-install.bat
javaam compile MyFile.jvm
```

### First Program

```javaam
main {
    print "Hello, World!"
}
```

Save as `hello.jvm`, then:
```bash
javaam compile hello.jvm
javac -cp . hello.java
java hello
```

---

## For Developers

### Build Compiler

```bash
cd compiler
mvn clean package
```

Output: `compiler/target/javaam-compiler-1.0.0.jar`

### Run Tests

```bash
cd compiler
mvn test
```

### Test Compiler Manually

```bash
cd compiler
mvn exec:java -Dexec.args="compile ../examples/HelloWorld.jvm"
```

---

## For Contributors

### Setup

```bash
git clone https://github.com/amethystdevs/javaam.git
cd javaam
```

### Make Changes

1. Edit source files
2. Update docs if needed
3. Test locally
4. Create pull request

### Run Extension Locally (VS Code)

```bash
cd javaam-vscode
code .
# Press F5 to launch debug instance
# Test in new VS Code window
```

---

## For Maintainers

### Release New Version

**Quick version:**
```bash
# 1. Update version in package.json files
# 2. Update CHANGELOG.md
# 3. Commit and tag
git tag v1.0.1
git push origin v1.0.1

# 4. Publish extension
cd javaam-vscode
npm run publish-patch  # or publish-minor, publish-major
```

**Detailed steps:** See [RELEASE.md](RELEASE.md) and [javaam-vscode/PUBLISH.md](javaam-vscode/PUBLISH.md)

### Create Windows Installer

```bash
# Prerequisites: NSIS (https://nsis.sourceforge.io/)

cd installer
makensis javaam-installer.nsi

# Output: javaam-1.0.1-installer.exe
```

### Create Portable Release

```bash
PowerShell -ExecutionPolicy Bypass -File scripts/build-release.ps1 -Version "1.0.1"

# Output: release/javaam-1.0.1.zip
```

---

## Project Structure

```
javaam/
├── compiler/              # Java compiler source (Maven project)
│   ├── pom.xml
│   ├── src/main/java/
│   └── target/
├── javaam-vscode/         # VS Code extension
│   ├── package.json
│   ├── src/extension.js
│   ├── PUBLISH.md
│   └── syntaxes/
├── installer/             # Installers
│   ├── javaam-install.sh  # Unix/Linux/macOS
│   ├── javaam-install.bat # Windows batch
│   └── javaam-installer.nsi # NSIS installer
├── scripts/               # Build scripts
│   └── build-release.ps1  # Release packager
├── examples/              # Example .jvm files
├── docs/                  # Language docs
├── RELEASE.md             # Publishing guide
├── README.md              # Project docs
└── CONTRIBUTING.md        # Contribution guide
```

---

## Environment Variables

For VS Code extension:

```bash
# Java location
JAVA_HOME=/path/to/java

# Maven location  
MAVEN_HOME=/path/to/maven

# (or standard: java and mvn on PATH)
```

---

## Common Tasks

| Task | Command |
|------|---------|
| Compile `.jvm` file | `javaam compile file.jvm` |
| Show help | `javaam help` |
| Run dev tools | `javaam dev help` |
| Package release | `PowerShell -File scripts/build-release.ps1 -Version "1.0.1"` |
| Publish extension | `cd javaam-vscode && npm run publish-patch` |
| Create installer | `makensis installer/javaam-installer.nsi` |
| Run tests | `cd compiler && mvn test` |

---

## Resources

- **GitHub**: https://github.com/amethystdevs/javaam
- **VS Code Marketplace**: https://marketplace.visualstudio.com/items?itemName=amethystdevs.javaam-vscode
- **Language Spec**: [docs/LANGUAGE_SPEC.md](docs/LANGUAGE_SPEC.md)
- **Setup Guide**: [SETUP.md](SETUP.md)
- **Cheatsheet**: [CHEATSHEET.md](CHEATSHEET.md)
