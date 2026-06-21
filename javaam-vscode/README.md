# JavaAm VS Code Extension

JavaAm VS Code Extension adds syntax highlighting and development tooling for the JavaAm Minecraft DSL.

## Features

- Syntax highlighting for JavaAm files and mod/plugin extensions
- `JavaAm: Compile Current File` command
- `JavaAm: Launch JVM mcmp dev` command for easy Minecraft mod/plugin project creation
- `JavaAm: Create Minecraft Mod/Plugin Project` command using repository templates

## Usage

Open a `.jvm` file and run `JavaAm: Compile Current File` from the command palette.
Use `JavaAm: Launch JVM mcmp dev` to access the new development helper.

## Requirements

- Java 8 or newer (`java` on PATH or `JAVA_HOME` set)
- Maven is recommended for compiler build and runtime, but a JDK with `javac` may also be used
- The `compiler` directory must be present in the workspace

## Extension Folder Structure

This extension includes:

- `package.json` for commands and language contributions
- `src/extension.js` for VS Code command execution
- `syntaxes/javaam.tmLanguage.json` for syntax highlighting
- `language-configuration.json` for comment and language support
