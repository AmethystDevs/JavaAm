# JavaAm Language Specification

This document defines the formal syntax and core semantics of JavaAm. It is the reference for how JavaAm source code is written and interpreted by the compiler.

## 1. Lexical Elements

### 1.1 Keywords

**Control Flow:**
- `if`, `else`, `for`, `while`, `return`, `break`, `continue`

**Declarations:**
- `class`, `fn`, `var`, `val`, `interface`, `enum`, `package`, `import`

**Inheritance:**
- `extends`, `implements`

**Modifiers:**
- `public`, `private`, `protected`, `static`, `final`, `abstract`

**Types:**
- `void`, `int`, `long`, `double`, `float`, `boolean`, `String`, `char`

**Literals:**
- `true`, `false`, `null`, `new`

### 1.2 Identifiers

Identifiers follow Java naming conventions:
- Start with a letter, underscore, or dollar sign
- Followed by letters, digits, underscores, or dollar signs
- Case-sensitive
- Cannot be keywords

### 1.3 Comments

**Line Comments:**
```javaam
// This is a line comment
```

**Block Comments:**
```javaam
/*
  This is a block comment
  spanning multiple lines
*/
```

### 1.4 String Literals

```javaam
var str = "Hello, World!";
var escaped = "Line 1\nLine 2";
```

### 1.5 Number Literals

```javaam
var integer = 42;
var decimal = 3.14;
var scientific = 1.5e10;
```

## 2. Types

### 2.1 Primitive Types

| Type | Range | Default |
|------|-------|---------|
| `int` | -2³¹ to 2³¹-1 | 0 |
| `long` | -2⁶³ to 2⁶³-1 | 0 |
| `float` | IEEE 754 | 0.0 |
| `double` | IEEE 754 | 0.0 |
| `boolean` | true / false | false |
| `char` | Unicode | '\u0000' |

### 2.2 Reference Types

- Classes
- Interfaces
- Arrays
- Enums

### 2.3 Type Inference

```javaam
var name = "Alice";
var age = 25;
var score: double = 95.5;
```

## 3. Variables and Constants

### 3.1 Variable Declaration

```javaam
var identifier: type = initializer;
var identifier = initializer;
```

### 3.2 Immutable Variables

```javaam
val PI: double = 3.14159;
val gravity: double = 9.8;
```

## 4. Methods

### 4.1 Method Declaration

```javaam
[modifiers] fn methodName: returnType(parameters) {
    // method body
}
```

### 4.2 Examples

```javaam
fn greet: void {
    System.out.println("Hello!");
}

fn add: int(a: int, b: int) {
    return a + b;
}

public fn getName: String {
    return "JavaAm";
}

private fn helper: void {
    System.out.println("Helper");
}

static fn staticMethod: void {
    System.out.println("Static");
}
```

## 5. Classes

### 5.1 Class Declaration

```javaam
[modifiers] class ClassName [extends SuperClass] [implements Interface1, Interface2] {
    // class members
}
```

### 5.2 Examples

```javaam
public class Animal {
    fn speak: void {
        System.out.println("...");
    }
}

class Dog extends Animal {
    fn speak: void {
        System.out.println("Woof!");
    }
}

class Circle implements Drawable {
    fn draw: void {
        System.out.println("Circle");
    }
}
```

## 6. Interfaces

### 6.1 Interface Declaration

```javaam
[modifiers] interface InterfaceName [extends SuperInterface] {
    // method signatures
}
```

### 6.2 Example

```javaam
interface Drawable {
    fn draw: void;
    fn getArea: double;
}
```

## 7. Control Structures

### 7.1 If-Else

```javaam
if (condition) {
    // statements
} else if (otherCondition) {
    // statements
} else {
    // statements
}
```

### 7.2 While Loop

```javaam
while (condition) {
    // statements
}
```

### 7.3 For-Each Loop

```javaam
for (variable: iterable) {
    // statements
}
```

### 7.4 Return Statement

```javaam
return value;
return;  // For void methods
```

## 8. Expressions

### 8.1 Operators

**Arithmetic:** `+`, `-`, `*`, `/`, `%`

**Comparison:** `<`, `>`, `<=`, `>=`, `==`, `!=`

**Logical:** `&&`, `||`, `!`

**Assignment:** `=`, `+=`, `-=`

### 8.2 Method Calls

```javaam
object.method();
object.method(arg1, arg2);
ClassName.staticMethod();
```

### 8.3 Object Creation

```javaam
var obj = new ClassName();
var obj = new ClassName(arg1, arg2);
```

### 8.4 Array Access

```javaam
var element = array[index];
array[index] = value;
```

## 9. Packages and Imports

### 9.1 Package Declaration

```javaam
package com.example.myapp;
```

### 9.2 Import Statement

```javaam
import java.util.List;
import java.util.*;
```

## 10. Minecraft-Specific Features

### 10.1 Fabric Mods (.fm.jvm)

Automatically includes:
- `net.fabricmc.api.ModInitializer`
- Fabric-specific APIs

### 10.2 NeoForge Mods (.nfm.jvm)

Automatically includes:
- NeoForge event system
- NeoForge utilities

### 10.3 Forge Mods (.fom.jvm)

Automatically includes:
- Forge event system
- Forge utilities

### 10.4 Paper Plugins (.pp.jvm)

Automatically includes:
- `org.bukkit.plugin.java.JavaPlugin`
- Paper API

### 10.5 Spigot Plugins (.sp.jvm)

Automatically includes:
- `org.bukkit.plugin.java.JavaPlugin`
- Spigot API

### 10.6 Purpur Plugins (.pu.jvm)

Automatically includes:
- `org.bukkit.plugin.java.JavaPlugin`
- Purpur API extensions

## 11. Compiler Directives

### 11.1 Compilation

```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar compile filename.jvm
```

## 12. Error Handling

### 12.1 Lexical Errors

- Unexpected characters
- Malformed strings
- Invalid number formats

### 12.2 Syntax Errors

- Missing semicolons
- Unmatched braces
- Invalid method signatures

### 12.3 Semantic Errors

- Undefined variables
- Type mismatches
- Invalid method calls

## 13. Best Practices

1. **Use descriptive names** for variables and methods
2. **Prefer `val` for constants** over `var`
3. **Use type inference** when types are obvious
4. **Keep methods small** and focused
5. **Use meaningful comments** for complex logic
6. **Follow naming conventions** (camelCase for variables, PascalCase for classes)

## 14. Example Programs

### 14.1 Calculator

```javaam
class Calculator {
    fn add: int(a: int, b: int) {
        return a + b;
    }

    fn subtract: int(a: int, b: int) {
        return a - b;
    }

    fn main: void {
        var result = new Calculator().add(5, 3);
        System.out.println(result);
    }
}
```

### 14.2 List Processing

```javaam
class ListProcessor {
    fn printList: void(items: List) {
        for (item: items) {
            System.out.println(item);
        }
    }

    fn main: void {
        var list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        new ListProcessor().printList(list);
    }
}
```

> Read the website version of this documentation in `webdoc/spec.html`.

