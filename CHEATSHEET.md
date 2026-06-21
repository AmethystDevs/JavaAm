# JavaAm Quick Reference

A quick cheat sheet for JavaAm syntax and features.

## Package & Imports

```javaam
package com.example;

import java.util.ArrayList;
import java.util.HashMap;
```

## Variables & Constants

```javaam
// Variable with type inference
var name = "Alice";                 // Inferred as String
var age = 25;                       // Inferred as int

// Variable with explicit type
var score: double = 95.5;
var count: long = 1000000L;

// Immutable constant
val PI = 3.14159;                  // Type inferred
val MAX_SIZE: int = 100;           // Explicit type
```

## Methods

```javaam
// Basic method
fn greet: void(name: String) {
    System.out.println("Hello, " + name);
}

// Method with return value
fn add: int(a: int, b: int) {
    return a + b;
}

// Method with multiple parameters
fn calculate: double(x: double, y: double, operation: String) {
    if (operation.equals("+")) return x + y;
    return x - y;
}

// Public/Private methods
public fn publicMethod: void { }
private fn privateMethod: void { }
```

## Classes & Inheritance

```javaam
// Basic class
class MyClass {
    var value: int;
    
    fn getValue: int { 
        return value; 
    }
}

// Class extending another
class Child extends Parent {
    fn childMethod: void { }
}

// Class implementing interfaces
class Impl implements Runnable, Comparable {
    fn run: void { }
    fn compareTo: int(other: Object) { return 0; }
}
```

## Data Types

| Type | Example | Size |
|------|---------|------|
| `int` | `42` | 32-bit |
| `long` | `1000000L` | 64-bit |
| `double` | `3.14` | 64-bit |
| `float` | `3.14f` | 32-bit |
| `boolean` | `true`, `false` | 1-bit |
| `String` | `"Hello"` | Variable |
| `char` | `'A'` | 16-bit Unicode |
| `void` | (no value) | No return |
| `byte` | `127` | 8-bit |
| `short` | `32767` | 16-bit |

## Operators

### Arithmetic

```javaam
a + b    // addition
a - b    // subtraction
a * b    // multiplication
a / b    // division
a % b    // modulo
```

### Comparison

```javaam
a == b   // equal
a != b   // not equal
a < b    // less than
a > b    // greater than
a <= b   // less or equal
a >= b   // greater or equal
```

### Logical

```javaam
a && b   // AND
a || b   // OR
!a       // NOT
```

### Assignment

```javaam
x = 5              // assign
x += 5             // add and assign
x -= 5             // subtract and assign
x *= 2             // multiply and assign
x /= 2             // divide and assign
x %= 10            // modulo and assign
```

## Arrays & Collections

```javaam
// Array declaration
var numbers: int[] = new int[10];
var strings: String[] = {"a", "b", "c"};

// Using ArrayList (from java.util)
var list = new ArrayList();
list.add("item1");
list.add("item2");
```

## Control Flow

### If Statement

```javaam
if (condition) {
    // do something
} else if (otherCondition) {
    // do something else
} else {
    // default
}
```

### While Loop

```javaam
while (condition) {
    // loop body
}
```

### For-Each Loop

```javaam
for (item: collection) {
    System.out.println(item);
}
```

### Return Statement

```javaam
return value;
return;  // for void methods
```

## Object-Oriented Features

### Inheritance

```javaam
class Parent {
    fn method: void {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    fn method: void {
        System.out.println("Child");
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

### Modifiers

```javaam
public fn publicMethod: void { }
private fn privateMethod: void { }
protected fn protectedMethod: void { }
static fn staticMethod: void { }
final fn finalMethod: void { }
abstract fn abstractMethod: void;
```

## Collections

### Arrays

```javaam
var arr: int[] = new int[10];
arr[0] = 5;
var item = arr[0];
```

### Lists

```javaam
var list = new ArrayList();
list.add(1);
list.add(2);
for (item: list) {
    System.out.println(item);
}
```

### Maps

```javaam
var map = new HashMap();
map.put("key", "value");
var value = map.get("key");
```

## Common Methods

### String

```javaam
var str = "hello";
str.length()              // 5
str.charAt(0)             // 'h'
str.substring(0, 3)       // "hel"
str.toUpperCase()         // "HELLO"
str.toLowerCase()         // "hello"
str.equals("hello")       // true
str.contains("ell")       // true
```

### System

```javaam
System.out.println("text");     // print with newline
System.out.print("text");       // print without newline
System.err.println("error");    // print to error stream
```

### Math

```javaam
Math.abs(-5)              // 5
Math.max(3, 5)            // 5
Math.min(3, 5)            // 3
Math.sqrt(16)             // 4.0
Math.pow(2, 3)            // 8.0
Math.random()             // 0.0 to 1.0
```

## File Extensions

| Extension | Purpose |
|-----------|---------|
| `.jvm` | Base JavaAm |
| `.fm.jvm` | Fabric Mod |
| `.nfm.jvm` | NeoForge Mod |
| `.fom.jvm` | Forge Mod |
| `.pp.jvm` | Paper Plugin |
| `.sp.jvm` | Spigot Plugin |
| `.pu.jvm` | Purpur Plugin |

## Minecraft-Specific

### Fabric Mod

```javaam
class MyMod implements ModInitializer {
    fn onInitialize: void {
        // mod init code
    }
}
```

### Paper Plugin

```javaam
class MyPlugin extends JavaPlugin {
    fn onEnable: void {
        // plugin startup
    }

    fn onDisable: void {
        // plugin shutdown
    }
}
```

## Code Examples

### Hello World

```javaam
class HelloWorld {
    fn main: void {
        System.out.println("Hello, World!");
    }
}
```

### Calculator

```javaam
class Calculator {
    fn add: int(a: int, b: int) {
        return a + b;
    }

    fn subtract: int(a: int, b: int) {
        return a - b;
    }

    fn multiply: int(a: int, b: int) {
        return a * b;
    }

    fn divide: double(a: double, b: double) {
        return a / b;
    }
}
```

### Loop Examples

```javaam
// Count to 10
var i: int = 0;
while (i < 10) {
    System.out.println(i);
    i = i + 1;
}

// Process list
var numbers = new ArrayList();
numbers.add(1);
numbers.add(2);
numbers.add(3);

for (num: numbers) {
    System.out.println(num * 2);
}
```

### Class Example
```javaam

class Person {
    var name: String;
    var age: int;

    fn greet: void {
        System.out.println("Hi, I'm " + this.name);
    }

    fn birthday: void {
        this.age = this.age + 1;
    }
}

class Main {
    fn main: void {
        var person = new Person();
        person.name = "Alice";
        person.age = 25;
        person.greet();
        person.birthday();
    }
}
```

## Keyboard Shortcuts (VS Code)

| Shortcut | Action |
|----------|--------|
| Ctrl+K Ctrl+C | Comment selection |
| Ctrl+K Ctrl+U | Uncomment selection |
| Ctrl+/ | Toggle comment |
| Alt+Up/Down | Move line up/down |
| Shift+Alt+Up/Down | Copy line up/down |
| Ctrl+X | Cut line |
| Ctrl+C | Copy |
| Ctrl+V | Paste |
| Ctrl+Z | Undo |
| Ctrl+Shift+Z | Redo |

## Tips & Tricks

1. **Use type inference** - Let JavaAm infer types when obvious
2. **Use val for constants** - Makes immutability clear
3. **Method names are verbs** - Use `getName()`, not `getNameData()`
4. **Keep methods small** - Single responsibility principle
5. **Use meaningful names** - `count` is better than `c`
6. **Comment complex logic** - But keep comments accurate
7. **Use Java libraries** - Full Java ecosystem available
8. **Test your code** - Write simple test cases

## Common Mistakes

| Mistake | Correct |
|---------|---------|
| `var x: int = "5"` | `var x: int = 5` |
| `fn method() { }` | `fn method: void { }` |
| `class Circle extends Circle` | (No circular inheritance) |
| `return 5;` in void | No return or just `return;` |
| Missing semicolon | `System.out.println("text");` |

## Performance Tips

1. Avoid unnecessary object creation
2. Use appropriate data structures
3. Minimize string concatenation loops
4. Use primitive types when possible
5. Cache frequently accessed values

## Debugging

```javaam
// Print values
System.out.println("Debug: " + variable);

// Assert conditions
assert value > 0 : "Value must be positive";

// Stack trace
try {
    // code
} catch (Exception e) {
    e.printStackTrace();
}
```

---

**Happy coding with JavaAm! 🚀**
