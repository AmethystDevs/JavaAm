# JavaAm - Project Overview

## Welcome to JavaAm! 🎉

JavaAm is a complete, simplified Java programming language that reduces boilerplate while maintaining full Java compatibility. Write cleaner code with built-in support for Minecraft modding and plugin development.

## Project Structure

```
JavaAm/
├── compiler/                           # JavaAm Compiler (Maven project)
│   ├── src/main/java/com/javaam/
│   │   ├── Token.java                 # Token type definitions and constants
│   │   ├── Lexer.java                 # Lexical analysis (source → tokens)
│   │   ├── Parser.java                # Syntax analysis (tokens → AST)
│   │   ├── ASTNode.java               # Abstract syntax tree node definitions
│   │   ├── CodeGenerator.java         # Code generation (AST → Java)
│   │   ├── JvmMcmpDev.java            # Development utilities
│   │   └── Main.java                  # CLI interface
│   ├── pom.xml                        # Maven configuration (Java 11+)
│   ├── target/                        # Built artifacts and compiled classes
│
├── javaam-vscode/                      # VS Code Language Extension
│   ├── package.json                   # Extension metadata and configuration
│   ├── language-configuration.json    # Language configuration (comments, brackets)
│   ├── src/extension.js               # Extension entry point
│   └── syntaxes/javaam.tmLanguage.json # TextMate grammar for syntax highlighting
│
├── examples/                           # Example Programs
│   ├── HelloWorld.jvm                 # Basic example
│   ├── FabricModExample.fm.jvm        # Fabric Mod
│   ├── NeoForgeModExample.nfm.jvm     # NeoForge Mod
│   ├── ForgeModExample.fom.jvm        # Forge Mod
│   ├── PaperPluginExample.pp.jvm      # Paper Plugin
│   ├── SpigotPluginExample.sp.jvm     # Spigot Plugin
│   └── PurpurPluginExample.pu.jvm     # Purpur Plugin
│
├── docs/                               # Documentation
│   ├── README.md                      # Language reference
│   └── LANGUAGE_SPEC.md               # Formal specification
│
├── build.sh                           # Linux/macOS build script
├── build.bat                          # Windows build script
├── README.md                          # Main project README
├── SETUP.md                           # Installation guide
├── CHEATSHEET.md                      # Quick reference
├── CONTRIBUTING.md                    # Contribution guidelines
├── LICENSE                            # MIT License
└── PROJECT_OVERVIEW.md                # This file
```

## Key Components

### 1. Compiler (`compiler/`)

The heart of JavaAm - transforms `.jvm` files into valid Java code.

**Main Classes:**
- **Token.java** - Defines all token types (keywords, operators, literals)
- **Lexer.java** - Scans source code and produces tokens
- **ASTNode.java** - Abstract syntax tree node definitions
- **Parser.java** - Parses tokens into AST
- **CodeGenerator.java** - Generates Java code from AST
- **Main.java** - Command-line interface

**Build & Run:**
```bash
# Build
cd compiler
mvn clean package

# Use
java -jar target/javaam-compiler-1.0.0.jar compile file.jvm
```

### 2. VS Code Extension (`javaam-vscode/`)

Provides language support for `.jvm` files in Visual Studio Code, including syntax highlighting and language configuration.

**Features:**
- **Syntax Highlighting** - Color-coded source code for better readability
- **Language Configuration** - Smart indentation, bracket matching, auto-formatting
- **File Association** - Automatic detection of `.jvm` files

**Installation:**
```bash
# Copy the extension folder to your VS Code extensions directory
# On Windows: %APPDATA%\Code\User\extensions
# On macOS: ~/.config/Code/User/extensions
# On Linux: ~/.config/Code/User/extensions

cp -r javaam-vscode ~/.config/Code/User/extensions/

# Then reload VS Code
```

### 3. Examples (`examples/`)

Real-world example programs for each use case:

| File | Type | Purpose |
|------|------|---------|
| HelloWorld.jvm | Base | Simple "Hello World" |
| FabricModExample.fm.jvm | Fabric Mod | Fabric modding |
| NeoForgeModExample.nfm.jvm | NeoForge Mod | NeoForge modding |
| ForgeModExample.fom.jvm | Forge Mod | Forge modding |
| PaperPluginExample.pp.jvm | Paper Plugin | Paper server plugin |
| SpigotPluginExample.sp.jvm | Spigot Plugin | Spigot server plugin |
| PurpurPluginExample.pu.jvm | Purpur Plugin | Purpur server plugin |

### 4. Documentation (`docs/`)

Comprehensive language documentation and reference materials:

- **LANGUAGE_SPEC.md** - Complete formal language specification
- **README.md** - Language overview and feature guide

### 5. Build Scripts

- **build.bat** - Windows build script (Maven wrapper)
- **build.sh** - Linux/macOS build script (Maven wrapper)

### 6. Root Documentation

- **README.md** - Main project overview and quick start
- **SETUP.md** - Installation and environment setup guide
- **CHEATSHEET.md** - Quick reference for common syntax patterns
- **CONTRIBUTING.md** - Guidelines for contributing to the project
- **PROJECT_OVERVIEW.md** - This file (detailed project breakdown)
- **LICENSE** - MIT License
- **CONTRIBUTING.md** - Community contribution guidelines

## Key Features

### 1. Simplified Syntax
- Use `fn` instead of verbose method declarations
- Type inference with `var` keyword
- Immutable variables with `val` keyword
- Reduced boilerplate code

### 2. Minecraft Support
Built-in support for multiple Minecraft modding and plugin platforms:
- **Fabric Mods** - Modern modding framework
- **NeoForge Mods** - Next-generation Forge successor
- **Forge Mods** - Legacy modding framework
- **Paper Plugins** - High-performance server plugin
- **Spigot Plugins** - Popular server plugin
- **Purpur Plugins** - Purpur server plugin

### 3. Full Java Compatibility
- Compiles directly to clean, idiomatic Java code
- Access to entire Java Standard Library
- Use any existing Java libraries and frameworks
- Interoperable with regular Java code

## Compilation Pipeline

```
Source Code (.jvm)
    ↓
Lexer (Tokenization)
    ↓
Parser (Syntax Analysis)
    ↓
AST (Abstract Syntax Tree)
    ↓
Code Generator
    ↓
Java Code (.java)
    ↓
Java Compiler
    ↓
Bytecode (.class)
```

## File Extensions

JavaAm files use `.jvm` extension with optional type suffixes:

| Extension | Type | Purpose |
|-----------|------|------|
| `.jvm` | Generic | General-purpose JavaAm code |
| `.fm.jvm` | Fabric Mod | Fabric modding code |
| `.nfm.jvm` | NeoForge Mod | NeoForge modding code |
| `.fom.jvm` | Forge Mod | Forge modding code |
| `.pp.jvm` | Paper Plugin | Paper plugin code |
| `.sp.jvm` | Spigot Plugin | Spigot plugin code |
| `.pu.jvm` | Purpur Plugin | Purpur plugin code |

## Getting Started

### For Users
1. Install Java JDK 11+ (see SETUP.md)
2. Install Maven (see SETUP.md)
3. Clone or download the repository
4. Run `build.bat` (Windows) or `./build.sh` (Linux/macOS)
5. Start writing JavaAm code!

### For Developers
1. Review CONTRIBUTING.md for contribution guidelines
2. Familiarize yourself with the compiler pipeline
3. Check existing issues for ways to help
4. Submit bug reports or feature requests

## Documentation Files

- **README.md** - Complete language reference with examples
- **LANGUAGE_SPEC.md** - Formal language specification

### 5. Build Scripts

- **build.sh** (Linux/macOS) - Automated build script
- **build.bat** (Windows) - Automated build script

Both scripts:
1. Clean previous builds
2. Compile with Maven
3. Package as JAR
4. Provide usage instructions

### 6. Documentation Files (Root)

- **README.md** - Project introduction and overview
- **SETUP.md** - Installation and setup guide
- **CHEATSHEET.md** - Quick language reference
- **CONTRIBUTING.md** - Guidelines for contributors
- **LICENSE** - MIT License

## File Extensions

JavaAm supports multiple file extensions for different purposes:

| Extension | Purpose | Compiler Type |
|-----------|---------|---------------|
| `.jvm` | Base JavaAm | base |
| `.fm.jvm` | Fabric Mod | fabric |
| `.nfm.jvm` | NeoForge Mod | neoforge |
| `.fom.jvm` | Forge Mod | forge |
| `.pp.jvm` | Paper Plugin | paper |
| `.sp.jvm` | Spigot Plugin | spigot |
| `.pu.jvm` | Purpur Plugin | purpur |

Each extension type automatically includes the appropriate framework imports and annotations.

## Language Features

### Simplified Syntax

```javaam
// Variables
var x = 5;                  // Type inferred
var name: String = "Bob";   // Explicit type
val pi = 3.14159;           // Immutable

// Methods
fn methodName: returnType(params) {
    // code
}

// Classes
class MyClass {
    fn method: void {
        // implementation
    }
}

// Control Flow
if (condition) { /* ... */ }
while (condition) { /* ... */ }
for (item: collection) { /* ... */ }
```

### Minecraft Support

Each framework type has automatic imports and helper methods:

```javaam
// Fabric Mod - automatically gets ModInitializer
// Forge Mod - automatically gets ForgeEvents
// Paper Plugin - automatically gets JavaPlugin
// etc.
```

### Full Java Compatibility

All generated Java code is valid and can use any Java library.

## Compilation Process

```
.jvm File
    ↓
[Lexer] → Tokens
    ↓
[Parser] → Abstract Syntax Tree
    ↓
[Code Generator] → Java Code
    ↓
.java File (or .class with javac)
```

## Getting Started

### 1. Quick Setup

```bash
# Navigate to project
cd JavaAm

# Build compiler (Windows)
build.bat

# Build compiler (Linux/macOS)
./build.sh
```

### 2. Compile Example

```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/HelloWorld.jvm
```

### 3. Compile Generated Java

```bash
javac examples/HelloWorld.java
```

### 4. Run

```bash
java HelloWorld
```

## File Type Detection

The compiler automatically detects the mod type from the file extension:

```java
.jvm → base
.fm.jvm → fabric
.nfm.jvm → neoforge
.fom.jvm → forge
.pp.jvm → paper
.sp.jvm → spigot
.pu.jvm → purpur
```

Different framework imports are added based on the detected type.

## Code Generation Example

**Input (JavaAm):**
```javaam
package com.example;

class Calculator {
    fn add: int(a: int, b: int) {
        return a + b;
    }
}
```

**Output (Java):**
```java
package com.example;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

## Configuration

The compiler uses Maven for build configuration. Key settings in `compiler/pom.xml`:

- **Java Version:** 11
- **Compiler Source/Target:** 11
- **JAR Configuration:** Main class set to `com.javaam.Main`

## Development

### For Compiler Development

1. Edit source files in `compiler/src/main/java/com/javaam/`
2. Build with `mvn clean package`
3. Test with compiled JAR

### For Language Features

1. Update Lexer.java for new tokens
2. Update Parser.java for new syntax
3. Update CodeGenerator.java for output
4. Add test examples

### For VS Code Extension

1. Edit files in `vscode-extension/`
2. Test in VS Code
3. Update package.json for metadata

## Testing Examples

Each example file in `examples/` demonstrates:
- Correct JavaAm syntax
- Framework-specific usage
- Best practices

Run compilation test:
```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/FabricModExample.fm.jvm
```

## Contribution Areas

**High Priority:**
- Lambda expression support
- Better error messages
- More test coverage

**Medium Priority:**
- Pattern matching
- Records
- Module system

**Low Priority:**
- IntelliJ plugin
- Web playground
- Additional examples

See CONTRIBUTING.md for guidelines.

## Performance

JavaAm generates highly optimized Java code with:
- Zero runtime overhead
- Direct compilation to bytecode
- Same performance as hand-written Java

## Compatibility Matrix

| Feature | Status |
|---------|--------|
| Basic Variables | ✅ |
| Methods | ✅ |
| Classes | ✅ |
| Interfaces | ✅ |
| Inheritance | ✅ |
| Control Flow | ✅ |
| Expressions | ✅ |
| Fabric Mods | ✅ |
| NeoForge Mods | ✅ |
| Forge Mods | ✅ |
| Paper Plugins | ✅ |
| Spigot Plugins | ✅ |
| Purpur Plugins | ✅ |
| Lambda Expressions | ⏳ |
| Pattern Matching | ⏳ |
| Records | ⏳ |

## Quick Links

- **Documentation:** `docs/README.md`
- **Language Spec:** `docs/LANGUAGE_SPEC.md`
- **Setup Guide:** `SETUP.md`
- **Quick Reference:** `CHEATSHEET.md`
- **Contributing:** `CONTRIBUTING.md`
- **Examples:** `examples/` directory

## License

JavaAm is licensed under the MIT License. See LICENSE file for details.

## Version Information

- **Version:** 1.0.0
- **Java Target:** 11+
- **Status:** Active Development

## Support & Community

- 📖 Check documentation in `docs/`
- 📋 Review examples in `examples/`
- 🤝 Contribute via CONTRIBUTING.md
- 🐛 Report issues with details

## Next Steps

1. **Read** [README.md](README.md) for overview
2. **Follow** [SETUP.md](SETUP.md) for installation
3. **Review** [CHEATSHEET.md](CHEATSHEET.md) for syntax
4. **Study** [Language Spec](docs/LANGUAGE_SPEC.md) for details
5. **Explore** `examples/` for real code
6. **Start** building your own projects!

---

**JavaAm - Simplify Your Java** 🚀

Built with ❤️ for Java developers everywhere.
