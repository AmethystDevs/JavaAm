# JavaAm Setup Guide

## Prerequisites

Before setting up JavaAm, ensure you have:

### Required
- **Java Development Kit (JDK) 11+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)

### Optional
- **Git** - For version control
- **Visual Studio Code** - For development

## Installation Steps

### 1. Install Java

#### Windows
1. Download JDK 11+ from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
2. Run the installer
3. Follow the installation wizard
4. Verify installation:
   ```cmd
   java -version
   javac -version
   ```

#### macOS
```bash
# Using Homebrew
brew install openjdk@11

# Or download from oracle.com
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install openjdk-11-jdk
java -version
```

### 2. Install Maven

#### Windows
1. Download Maven from [maven.apache.org](https://maven.apache.org/download.cgi)
2. Extract to a folder (e.g., `C:\apache-maven-3.8.1`)
3. Add to System Environment Variables:
   - `MAVEN_HOME`: `C:\apache-maven-3.8.1`
   - Add to `PATH`: `%MAVEN_HOME%\bin`
4. Verify:
   ```cmd
   mvn -version
   ```

#### macOS
```bash
# Using Homebrew
brew install maven

# Verify
mvn -version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install maven
mvn -version
```

### 3. Build JavaAm Compiler

```bash
# Navigate to project directory
cd JavaAm

# Option 1: On Windows
build.bat

# Option 2: On macOS/Linux
chmod +x build.sh
./build.sh

# Option 3: Manual Maven build
cd compiler
mvn clean package
cd ..
```

**Build Artifacts:**
- `compiler/target/javaam-compiler-1.0.0.jar` - Executable compiler JAR
- `compiler/target/classes/` - Compiled class files

**Verification:**
```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar help
```

### 4. Set Up VS Code Extension (Optional)

#### Install Visual Studio Code
1. Download from [code.visualstudio.com](https://code.visualstudio.com/)
2. Install the application
3. Launch VS Code

#### Install JavaAm Language Extension
1. Copy the `javaam-vscode` folder to your VS Code extensions directory:
   
   **Windows:**
   ```
   %USERPROFILE%\AppData\Roaming\Code\User\extensions\javaam-lang
   ```
   
   **macOS:**
   ```
   ~/.config/Code/User/extensions/javaam-lang
   ```
   
   **Linux:**
   ```
   ~/.config/Code/User/extensions/javaam-lang
   ```

2. Reload VS Code (Ctrl+R or Cmd+R)

3. Open a `.jvm` file to test syntax highlighting

## Compilation

### Quick Start

```bash
# Compile a JavaAm file
java -jar compiler/target/javaam-compiler-1.0.0.jar compile HelloWorld.jvm

# Output: HelloWorld.java
```

### Compile Examples

```bash
# Basic example
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/HelloWorld.jvm

# Fabric mod
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/FabricModExample.fm.jvm

# Paper plugin
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/PaperPluginExample.pp.jvm
```

### Using Generated Java Files

```bash
# Compile the generated Java
javac HelloWorld.java

# Run the program
java HelloWorld
```

## File Organization

After setup, your directory should look like:

```
JavaAm/
├── compiler/
│   ├── src/
│   ├── target/
│   │   └── javaam-compiler-1.0.0.jar  ← Main compiler
│   └── pom.xml
├── vscode-extension/
│   ├── package.json
│   ├── syntaxes/
│   └── language-configuration.json
├── examples/
│   ├── HelloWorld.jvm
│   ├── FabricModExample.fm.jvm
│   └── PaperPluginExample.pp.jvm
├── docs/
│   ├── README.md
│   └── LANGUAGE_SPEC.md
├── build.sh
├── build.bat
└── README.md
```

## Creating Your First Program

### 1. Create a new file

**MyProgram.jvm:**
```javaam
package com.example;

class MyProgram {
    fn main: void {
        var name = "JavaAm";
        System.out.println("Hello, " + name + "!");
    }
}
```

### 2. Compile to Java

```bash
java -jar compiler/target/javaam-compiler-1.0.0.jar compile MyProgram.jvm
```

### 3. View generated Java

**MyProgram.java:**
```java
package com.example;

class MyProgram {
    void main() {
        String name = "JavaAm";
        System.out.println("Hello, " + name + "!");
    }
}
```

## Troubleshooting

### Maven not found
**Solution:** Ensure Maven is in your PATH:
```bash
# Windows
set PATH=%PATH%;C:\apache-maven-3.8.1\bin

# macOS/Linux
export PATH=$PATH:/usr/local/Cellar/maven/3.8.1/libexec/bin
```

### Java version mismatch
**Solution:** Use Java 11+:
```bash
java -version
# Should show 11 or higher
```

### Build fails
**Solution:** Clean and rebuild:
```bash
cd compiler
mvn clean
mvn package
cd ..
```

### VS Code extension not loading
**Solution:** 
1. Reload VS Code (Ctrl+R)
2. Check extension directory path
3. Verify `package.json` is in the correct location

## Creating Minecraft Mods/Plugins

### Fabric Mod Template

**MyMod.fm.jvm:**
```javaam
package com.example.myfabricmod;

class MyFabricMod implements ModInitializer {
    fn onInitialize: void {
        System.out.println("Fabric mod loaded!");
    }
}
```

### Paper Plugin Template

**MyPlugin.pp.jvm:**
```javaam
package com.example.mypaperplugin;

class MyPlugin extends JavaPlugin {
    fn onEnable: void {
        this.getLogger().info("Plugin enabled!");
    }

    fn onDisable: void {
        this.getLogger().info("Plugin disabled!");
    }
}
```

## Advanced Configuration

### Custom Compiler Options

Create a `javaam.config` file:
```properties
# JavaAm Compiler Configuration
target.version=11
output.directory=build/
debug=false
```

### Maven POM Extensions

Extend the compiler's `pom.xml` for additional dependencies:
```xml
<dependencies>
    <dependency>
        <groupId>net.fabricmc</groupId>
        <artifactId>fabric-loader</artifactId>
        <version>0.14.0</version>
    </dependency>
</dependencies>
```

## Next Steps

1. Read the [Language Reference](../docs/README.md)
2. Check out [Examples](../examples/)
3. Review the [Language Specification](../docs/LANGUAGE_SPEC.md)
4. Start building your projects!

## Getting Help

- **Documentation:** See `docs/` directory
- **Examples:** See `examples/` directory
- **Issues:** Report bugs on GitHub
- **Community:** Join our Discord server

## Uninstallation

To remove JavaAm:

```bash
# Remove compiler
rm -rf compiler/

# Remove VS Code extension
rm -rf ~/.config/Code/User/extensions/javaam-lang

# Remove cloned repository
rm -rf JavaAm/
```

---

**Happy Coding! 🚀**
