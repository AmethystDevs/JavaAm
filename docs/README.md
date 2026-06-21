# JavaAm Programming Language

JavaAm is a simplified Java programming language that compiles directly to Java. It reduces boilerplate, improves readability, and preserves compatibility with the Java ecosystem.

> This documentation is also available as a website under `webdoc/`.

## Features

- **Simplified Syntax**: Cleaner, more readable code compared to Java
- **Type Inference**: Automatic type detection when not explicitly specified
- **Shorter Method Syntax**: Use `fn` instead of verbose Java method declarations
- **Modern Language Patterns**: Lambda-friendly design and clearer flow control
- **Minecraft Support**: Built-in support for Fabric, NeoForge, Forge, Paper, Spigot, and Purpur
- **Java Interoperability**: Compiles to valid Java code and can use Java libraries directly

## File Extensions

| Extension | Purpose |
|-----------|---------|
| `.jvm` | Base JavaAm file |
| `.fm.jvm` | Fabric Mod |
| `.nfm.jvm` | NeoForge Mod |
| `.fom.jvm` | Forge Mod |
| `.pp.jvm` | Paper Plugin |
| `.sp.jvm` | Spigot Plugin |
| `.pu.jvm` | Purpur Plugin |

## Language Basics

### Hello World

```javaam
package com.example;

class HelloWorld {
    fn main: void {
        var message = "Hello, JavaAm!";
        System.out.println(message);
    }
}
```

### Variables and Types

```javaam
// Type inference
var name = "Alice";

// Explicit typing
var age: int = 25;

// Immutable variable
val pi: double = 3.14159;
```

### Methods

```javaam
class Calculator {
    // Method with return type
    fn add: int(a: int, b: int) {
        return a + b;
    }

    // Void method
    fn greet: void(name: String) {
        System.out.println("Hello, " + name);
    }

    // Access modifiers
    public fn multiply: int(a: int, b: int) {
        return a * b;
    }

    private fn helper: void {
        System.out.println("Helper method");
    }
}
```

### Control Flow

```javaam
// If statement
if (age > 18) {
    System.out.println("Adult");
} else {
    System.out.println("Minor");
}

// While loop
var i: int = 0;
while (i < 10) {
    System.out.println(i);
    i = i + 1;
}

// For-each loop
for (item: list) {
    System.out.println(item);
}
```

### Classes

```javaam
class Animal {
    fn speak: void {
        System.out.println("Some sound");
    }
}

class Dog extends Animal {
    fn speak: void {
        System.out.println("Woof!");
    }
}

class Main {
    fn main: void {
        var dog = new Dog();
        dog.speak();
    }
}
```

## Web Documentation

The website version of this documentation is available in the `webdoc/` folder. Open `webdoc/index.html` in your browser for an interactive navigation experience.

    }
}
```

### Interfaces

```javaam
interface Drawable {
    fn draw: void;
}

class Circle implements Drawable {
    fn draw: void {
        System.out.println("Drawing circle");
    }
}
```

## Minecraft Development

### Fabric Mod Example

```javaam
// ExampleMod.fm.jvm
package com.example.fabricmod;

class ExampleMod implements ModInitializer {
    fn onInitialize: void {
        var logger = LoggerFactory.getLogger("example-mod");
        logger.info("Mod loaded!");
    }
}
```

### Paper Plugin Example

```javaam
// ExamplePlugin.pp.jvm
package com.example.paperplugin;

class ExamplePlugin extends JavaPlugin {
    fn onEnable: void {
        var logger = this.getLogger();
        logger.info("Plugin enabled!");
    }

    fn onDisable: void {
        var logger = this.getLogger();
        logger.info("Plugin disabled!");
    }
}
```

## Installation

### Requirements
- Java 11 or higher
- Maven 3.6+

### Build from Source

```bash
cd compiler
mvn clean package
```

### Using the Compiler

```bash
javaam compile MyFile.jvm
```

The compiler will generate a corresponding Java file that can be compiled with `javac`.

## VS Code Extension

Install the JavaAm language extension for syntax highlighting:

1. Copy the `vscode-extension` folder to your VS Code extensions directory
2. Reload VS Code
3. Open `.jvm` files and enjoy syntax highlighting!

## Syntax Comparison: JavaAm vs Java

| Feature | JavaAm | Java |
|---------|--------|------|
| Variable declaration | `var x = 5;` | `int x = 5;` |
| Immutable variable | `val x: int = 5;` | `final int x = 5;` |
| Method | `fn getName: String { ... }` | `String getName() { ... }` |
| Class | `class MyClass { ... }` | `class MyClass { ... }` |
| Inheritance | `class Dog extends Animal` | `class Dog extends Animal` |
| Interface impl. | `class Circle implements Drawable` | `class Circle implements Drawable` |
| For-each | `for (item: list)` | `for (Item item : list)` |

## Compiler Output

JavaAm compiles to clean, readable Java code. For example:

**Input (JavaAm):**
```javaam
var message: String = "Hello";
System.out.println(message);
```

**Output (Java):**
```java
String message = "Hello";
System.out.println(message);
```

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests.

## License

MIT License - See LICENSE file for details

## Support

For issues, questions, or feature requests, please visit:
https://github.com/javaam/javaam

## Roadmap

- [ ] Lambda expressions
- [ ] Pattern matching
- [ ] Records
- [ ] Module system
- [ ] Standard library utilities
- [ ] Package manager
- [ ] IDE plugins (IntelliJ, Eclipse)

## Acknowledgments

JavaAm is inspired by Kotlin, Scala, and Go, adapting their simplicity for Java developers.
