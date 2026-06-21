# Contributing to JavaAm

Thank you for your interest in contributing to JavaAm! We appreciate all contributions, whether they're bug reports, feature requests, documentation improvements, or code enhancements.

## Code of Conduct

We are committed to providing a welcoming and inclusive environment for all contributors. Please:
- Be respectful and constructive in all interactions
- Give credit and acknowledge contributions
- Report issues responsibly and privately if they're security-related
- Focus on the code and ideas, not on individuals

## How to Contribute

### Reporting Bugs

1. **Check existing issues** to avoid duplicates
2. **Provide a clear description** of the bug
3. **Include reproduction steps** with a minimal example
4. **Specify your environment** (OS, Java version, Maven version)
5. **Attach relevant files** or error logs

**Bug Report Template:**
```markdown
## Description
[Clear description of the bug]

## Reproduction Steps
1. [First step]
2. [Second step]
3. [...]

## Expected Behavior
[What should happen]

## Actual Behavior
[What actually happens]

## Environment
- OS: [Windows/macOS/Linux]
- Java: [version]
- Maven: [version]
```

### Suggesting Features

1. **Check existing issues** for similar requests
2. **Describe the feature** and its use case
3. **Provide examples** of how it would be used
4. **Explain the benefits** of the feature

**Feature Request Template:**
```markdown
## Feature Description
[Clear description]

## Use Case
[Why is this useful?]

## Example Usage
```javaam
// How would this be used?
```

## Proposed Solution
[How could this be implemented?]
```

### Submitting Code

1. **Fork the repository**
   ```bash
   git clone https://github.com/your-username/javaam.git
   cd javaam
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/my-feature
   # Or for bug fixes:
   git checkout -b fix/bug-description
   ```

3. **Make your changes**
   - Follow existing code style and conventions
   - Write clean, readable, well-commented code
   - Add meaningful comments for complex logic
   - Keep changes focused and atomic

4. **Build and test your changes**
   ```bash
   # Build the compiler
   cd compiler
   mvn clean package
   
   # Run tests
   mvn clean test
   
   # Test with examples
   cd ..
   java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/HelloWorld.jvm
   ```

5. **Commit with clear, descriptive messages**
   ```bash
   # Good commit messages
   git commit -m "feat: Add support for multi-line comments"
   git commit -m "fix: Correct type inference for array literals"
   git commit -m "docs: Update LANGUAGE_SPEC with new features"
   ```

   Follow the format: `<type>: <description>`
   - `feat:` - A new feature
   - `fix:` - A bug fix
   - `docs:` - Documentation changes
   - `refactor:` - Code refactoring without behavior change
   - `test:` - Test additions or updates
   - `chore:` - Build, CI, or tooling changes

6. **Push to your fork**
   ```bash
   git push origin feature/my-feature
   ```

7. **Create a Pull Request**
   - Use a clear, descriptive title
   - Provide a detailed description of changes
   - Link related issues (e.g., "Fixes #123")
   - Include before/after examples if applicable
   - Ensure all checks pass

**PR Description Template:**
```markdown
## Description
Brief description of the changes

## Type of Change
- [ ] Bug fix (non-breaking change that fixes an issue)
- [ ] New feature (non-breaking change that adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to change)
- [ ] Documentation update

## Related Issues
Fixes #(issue number)

## Testing
Describe the testing done to verify this change:
- [ ] Tested with existing examples
- [ ] Added new tests
- [ ] Tested on Windows/macOS/Linux

## Checklist
- [ ] Code follows project style guidelines
- [ ] No new compiler warnings
- [ ] Documentation updated
- [ ] Tests pass locally
- [ ] Commit messages are clear and descriptive
```

## Code Style Guidelines

### Java Compiler Code

- **Naming:**
  - Classes: `PascalCase` (e.g., `CodeGenerator`)
  - Methods: `camelCase` (e.g., `generateCode()`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `MAX_TOKENS`)
  - Variables: `camelCase` (e.g., `currentToken`)

- **Formatting:**
  - Use 4 spaces for indentation (no tabs)
  - Line length: Keep under 100 characters when possible
  - Braces: Opening brace on same line (Java convention)
  - Imports: Organize alphabetically, remove unused imports

- **Comments:**
  - Use JavaDoc for public methods: `/** ... */`
  - Use line comments `//` for inline explanations
  - Keep comments concise and meaningful
  - Update comments when changing code logic

### JavaAm Language Features

When adding new language features:
- Add tests covering the new feature
- Update `LANGUAGE_SPEC.md` with formal syntax
- Add examples to `CHEATSHEET.md`
- Update relevant compiler classes (Lexer, Parser, CodeGenerator)
- Document any breaking changes

## Testing

### Running Tests

```bash
# Run all tests
cd compiler
mvn clean test

# Run specific test class
mvn test -Dtest=LexerTest

# Run with coverage
mvn clean test jacoco:report
```

### Writing Tests

- Use JUnit 4 framework
- Test name format: `test<FeatureName><Scenario>`
- Include both positive and negative test cases
- Keep tests focused and independent
- Use descriptive assertion messages

### Manual Testing

```bash
# Compile an example
java -jar compiler/target/javaam-compiler-1.0.0.jar compile examples/HelloWorld.jvm

# Verify generated code compiles with javac
javac HelloWorld.java

# Run the generated code
java HelloWorld
```

## Documentation

### Updating Documentation

- **LANGUAGE_SPEC.md** - Formal language grammar and features
- **CHEATSHEET.md** - Common syntax patterns and examples
- **README.md** - Project overview and getting started
- **SETUP.md** - Installation and configuration
- **docs/README.md** - Language reference guide

### Writing Good Documentation

- Use clear, concise language
- Include code examples where helpful
- Keep markdown syntax simple and readable
- Update table of contents for major changes
- Proofread for grammar and spelling

## Development Setup

### IDE Setup (IntelliJ IDEA)

1. Open project as Maven project
2. Configure JDK to Java 11+
3. Mark `compiler/src/main/java` as Sources root
4. Mark `compiler/src/test/java` as Tests root

### IDE Setup (Eclipse)

1. Import project as Existing Maven Projects
2. Configure workspace JRE to Java 11+
3. Project should build automatically

### Building from Command Line

```bash
# Full build with tests
mvn clean package

# Build without tests
mvn clean package -DskipTests

# View project structure
mvn project-info-reports:dependencies
```

## Reporting Security Issues

**Please do not open public issues for security vulnerabilities.**

Instead, email security concerns to: security@javaam.example.com

Include:
- Description of the vulnerability
- Steps to reproduce
- Potential impact
- Suggested fix (if you have one)

## Getting Help

- Check existing [issues](https://github.com/your-username/javaam/issues)
- Review [LANGUAGE_SPEC.md](docs/LANGUAGE_SPEC.md) for language details
- Ask in issue comments or discussions
- Read the [README.md](README.md) for overview

## Recognition

All contributors are recognized:
- In commit history
- In release notes for significant contributions
- In a CONTRIBUTORS.md file (coming soon)

## Questions?

- Open a GitHub Issue for clarification
- Create a Discussion for longer conversations
- Check closed issues and PRs for similar questions

