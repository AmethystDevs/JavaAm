# JavaAm - A Simplified Java Programming Language

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java 11+](https://img.shields.io/badge/Java-11%2B-blue)
![Status: Active Development](https://img.shields.io/badge/Status-Active%20Development-brightgreen)

JavaAm is a modern programming language that simplifies Java while maintaining full compatibility with the Java ecosystem. Write cleaner, more readable code with reduced boilerplate while leveraging the power of Java and the JVM.

**Perfect for:**
- 🚀 **Quick Projects** - Get started with minimal setup
- 🎮 **Minecraft Modding** - Built-in support for Fabric, Forge, NeoForge, and Plugins
- 📚 **Learning** - Cleaner syntax makes Java concepts easier to understand
- ⚡ **Production Code** - Compiles to standard Java, runs everywhere

## Key Features

✨ **Simplified Syntax**
- `fn` keyword for methods instead of verbose declarations
- Type inference with `var` keyword
- Immutable variables with `val` keyword  
- Reduced boilerplate and ceremony

🎮 **Minecraft Support**
- Fabric Mods (`.fm.jvm`)
- NeoForge Mods (`.nfm.jvm`)
- Forge Mods (`.fom.jvm`)
- Paper Plugins (`.pp.jvm`)
- Spigot Plugins (`.sp.jvm`)
- Purpur Plugins (`.pu.jvm`)

☕ **Full Java Compatibility**
- Compiles to clean, idiomatic Java code
- Access entire Java Standard Library
- Use any existing Java libraries and frameworks
- 100% interoperable with regular Java code

🔧 **Developer Friendly**
- VS Code language extension with syntax highlighting
- Clear error messages and diagnostics
- Comprehensive documentation and examples
- Active community support

## Quick Start

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Maven 3.6 or higher
- Git (optional)

### Installation & Setup

```bash
# 1. Clone or download the repository
git clone https://github.com/your-username/javaam.git
cd javaam

# 2. Build the compiler
./build.sh              # On macOS/Linux
build.bat              # On Windows

# 3. Verify installation
java -jar compiler/target/javaam-compiler-1.0.0.jar help
```

See [SETUP.md](SETUP.md) for detailed installation instructions.

### Your First Program

**HelloWorld.jvm:**
```javaam
package com.example;

class HelloWorld {
    fn main: void {
        var message = "Hello, JavaAm!";
        System.out.println(message);
    }
}
```

**Compile it:**
```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar compile HelloWorld.jvm
```

**View the generated Java:**
```bash
cat HelloWorld.java
```

## Project Structure

```
javaam/
├── compiler/                      # JavaAm Compiler (Maven project)
│   ├── src/main/java/com/javaam/
│   │   ├── Main.java             # CLI entry point
│   │   ├── Lexer.java            # Tokenization
│   │   ├── Parser.java           # Syntax analysis
│   │   ├── ASTNode.java          # Abstract syntax tree
│   │   ├── CodeGenerator.java    # Java code generation
│   │   ├── Token.java            # Token definitions
│   │   └── JvmMcmpDev.java       # Development utilities
│   ├── pom.xml                   # Maven build configuration
│   └── target/                   # Build artifacts
│
├── javaam-vscode/                # VS Code Extension
│   ├── package.json              # Extension metadata
│   ├── language-configuration.json # Language config
│   ├── src/extension.js          # Extension code
│   └── syntaxes/javaam.tmLanguage.json # Syntax highlighting
│
├── examples/                     # Example Programs
│   ├── HelloWorld.jvm
│   ├── FabricModExample.fm.jvm
│   ├── NeoForgeModExample.nfm.jvm
│   ├── ForgeModExample.fom.jvm
│   ├── PaperPluginExample.pp.jvm
│   ├── SpigotPluginExample.sp.jvm
│   └── PurpurPluginExample.pu.jvm
│
├── docs/                        # Documentation
│   ├── LANGUAGE_SPEC.md         # Complete language specification
│   └── README.md                # Language reference
│
├── build.sh                     # Build script (macOS/Linux)
├── build.bat                    # Build script (Windows)
├── README.md                    # This file
├── SETUP.md                     # Installation guide
├── CHEATSHEET.md                # Quick syntax reference
├── CONTRIBUTING.md              # Contribution guidelines
├── PROJECT_OVERVIEW.md          # Detailed project overview
├── LICENSE                      # MIT License
└── CONTRIBUTING.md              # How to contribute
```

## Language Examples

### Variables & Type Inference

```javaam
var name = "Alice";              // String (inferred)
var age = 25;                    // int (inferred)
var score: double = 95.5;        // Explicit type
val MAX_SIZE: int = 100;         // Immutable constant
```

### Methods

```javaam
// Method with parameters and return value
fn add: int(a: int, b: int) {
    return a + b;
}

// Public method
public fn greet: void(name: String) {
    System.out.println("Hello, " + name);
}

// Method with no parameters
fn getCurrentTime: long {
    return System.currentTimeMillis();
}
```

### Classes & Inheritance

```javaam
// Basic class
class Animal {
    var name: String;
    
    fn speak: void {
        System.out.println("Some sound");
    }
}

// Class inheritance
class Dog extends Animal {
    fn speak: void {
        System.out.println("Woof!");
    }
}

// Interface implementation
class Runner implements Runnable {
    fn run: void {
        System.out.println("Running...");
    }
}
```

### Control Flow

```javaam
// If-else
if (x > 10) {
    System.out.println("Greater");
} else {
    System.out.println("Lesser or equal");
}

// Loops
for (var i = 0; i < 10; i++) {
    System.out.println(i);
}

while (condition) {
    // code
}
```

## Compiler Commands

```bash
# Show help
java -jar compiler/target/javaam-compiler-1.0.0.jar help

# Compile a single file
java -jar compiler/target/javaam-compiler-1.0.0.jar compile MyProgram.jvm

# Development mode
java -jar compiler/target/javaam-compiler-1.0.0.jar dev
```

## Documentation

- **[CHEATSHEET.md](CHEATSHEET.md)** - Quick syntax reference and common patterns
- **[SETUP.md](SETUP.md)** - Detailed installation and environment setup
- **[docs/LANGUAGE_SPEC.md](docs/LANGUAGE_SPEC.md)** - Complete language specification
- **[PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md)** - Detailed project structure and components
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Guidelines for contributing

## Getting Help

- 📖 Read the [Language Specification](docs/LANGUAGE_SPEC.md)
- 💡 Check the [Quick Reference](CHEATSHEET.md) for syntax examples
- 📁 Explore the [examples/](examples/) folder for real-world code
- 🐛 Report bugs on [GitHub Issues](https://github.com/your-username/javaam/issues)
- 🤝 Contribute improvements via [Pull Requests](https://github.com/your-username/javaam/pulls)

## VS Code Extension

The JavaAm VS Code extension provides:
- 🎨 Syntax highlighting for `.jvm` files
- 📝 Language configuration (comments, brackets, indentation)
- 🔍 File type association and auto-detection

**Installation:**
1. Copy `javaam-vscode` folder to your VS Code extensions directory
2. Reload VS Code
3. Open a `.jvm` file to see syntax highlighting in action

See [SETUP.md](SETUP.md) for platform-specific installation instructions.

## Examples

The `examples/` folder contains ready-to-use programs:

- **HelloWorld.jvm** - Classic "Hello, World!" program
- **FabricModExample.fm.jvm** - Fabric mod template
- **NeoForgeModExample.nfm.jvm** - NeoForge mod template  
- **ForgeModExample.fom.jvm** - Forge mod template
- **PaperPluginExample.pp.jvm** - Paper plugin template
- **SpigotPluginExample.sp.jvm** - Spigot plugin template
- **PurpurPluginExample.pu.jvm** - Purpur plugin template

Compile any example:
```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/HelloWorld.jvm
```

## Why JavaAm?

### Problems It Solves
- **Verbose Syntax** - Java requires a lot of boilerplate code
- **Learning Curve** - Too many concepts for beginners
- **Fast Prototyping** - Hard to write quick scripts in Java
- **Module Fatigue** - Complex modding frameworks have steep learning curves

### JavaAm Solutions
- Clean, concise syntax that's easy to read
- Reduced boilerplate without sacrificing power
- Full Java compatibility for production-ready code
- Specialized support for Minecraft modding

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for:
- How to report bugs
- How to suggest features
- How to submit code changes
- Code style guidelines
- Pull request process

## License

JavaAm is released under the [MIT License](LICENSE). See the LICENSE file for complete details.

## Roadmap

Planned features and improvements:
- [ ] Performance optimizations
- [ ] Extended standard library
- [ ] IDE plugins (IntelliJ, Eclipse)
- [ ] Package manager
- [ ] Online playground
- [ ] Package management and dependency resolution
- [ ] Additional language features

## Community

- ⭐ Star the repository if you find it useful
- 🐛 Report issues and suggest features
- 🤝 Contribute code and documentation
- 💬 Share your JavaAm projects

## Acknowledgments

JavaAm is inspired by languages like Kotlin, Scala, and Groovy, adapting their elegant syntax for Java developers.

---

**Ready to get started? Check out [SETUP.md](SETUP.md) for installation instructions!**


## Performance

JavaAm compiles to optimized Java code with no runtime overhead. Compiled programs have the same performance as hand-written Java.

## Compatibility

- **Java Version:** 11+
- **OS:** Windows, macOS, Linux
- **Target:** Java applications, Minecraft mods/plugins

## Contributing

Contributions welcome! Areas for improvement:

- [ ] Lambda expression support
- [ ] Pattern matching
- [ ] Records
- [ ] Better error messages
- [ ] Module system
- [ ] Standard library

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## License

MIT License - See [LICENSE](LICENSE) file for details

## Roadmap

### v1.0 (Current)
- Core language features
- Basic Minecraft support
- VS Code extension
- Compiler

### v1.1
- Lambda expressions
- Enhanced error messages
- Performance improvements

### v1.2
- Pattern matching
- Records
- Sealed classes

### v2.0
- Package manager
- Standard library
- IDE plugin support

## FAQ

**Q: Is JavaAm production-ready?**
A: JavaAm is actively being developed. It's suitable for learning and small projects. Use with caution in production.

**Q: Can I use existing Java libraries?**
A: Yes! JavaAm generates valid Java code, so you can use any Java library.

**Q: How does performance compare to Java?**
A: Performance is identical since JavaAm compiles to Java bytecode.

**Q: Can I use JavaAm for Minecraft modding?**
A: Yes! JavaAm has first-class support for Fabric, Forge, NeoForge, Paper, Spigot, and Purpur.

## Support

- 📖 [Documentation](docs/)
- 🐛 [Issue Tracker](https://github.com/javaam/javaam/issues)
- 💬 [Discussions](https://github.com/javaam/javaam/discussions)

## Acknowledgments

JavaAm is inspired by:
- Kotlin
- Scala
- Go
- Python

Special thanks to the Java and Minecraft communities!

---

**JavaAm** - *Simplify Your Java*
