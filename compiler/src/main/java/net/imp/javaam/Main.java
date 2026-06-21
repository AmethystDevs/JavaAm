package com.javaam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        String command = args[0];

        if (command.equals("compile") && args.length >= 2) {
            compileFile(args[1]);
        } else if (command.equals("dev")) {
            try {
                JvmMcmpDev.run(Arrays.copyOfRange(args, 1, args.length));
            } catch (IOException e) {
                System.err.println("IO Error: " + e.getMessage());
                System.exit(1);
            }
        } else if (command.equals("help")) {
            printHelp();
        } else {
            System.err.println("Unknown command: " + command);
            printUsage();
            System.exit(1);
        }
    }

    private static void compileFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Error: File not found: " + filePath);
                System.exit(1);
            }

            // Detect mod type from extension
            String modType = detectModType(filePath);

            // Read file
            String source = new String(Files.readAllBytes(Paths.get(filePath)));

            // Tokenize
            System.out.println("[*] Tokenizing...");
            Lexer lexer = new Lexer(source);
            java.util.List<Token> tokens = lexer.tokenize();

            // Parse
            System.out.println("[*] Parsing...");
            Parser parser = new Parser(tokens);
            Program program = parser.parse();

            // Generate code
            System.out.println("[*] Generating Java code...");
            CodeGenerator generator = new CodeGenerator(modType);
            String javaCode = generator.generate(program);

            // Write output
            String outputPath = filePath.replaceAll("\\..*\\.jvm$", ".java");
            Files.write(Paths.get(outputPath), javaCode.getBytes());

            System.out.println("[+] Compilation successful!");
            System.out.println("[+] Output: " + outputPath);

        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Compilation Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String detectModType(String filePath) {
        if (filePath.endsWith(".fm.jvm")) return "fm"; // Fabric Mod
        if (filePath.endsWith(".nfm.jvm")) return "nfm"; // NeoForge Mod
        if (filePath.endsWith(".fom.jvm")) return "fom"; // Forge Mod
        if (filePath.endsWith(".pp.jvm")) return "pp"; // Paper Plugin
        if (filePath.endsWith(".sp.jvm")) return "sp"; // Spigot Plugin
        if (filePath.endsWith(".pu.jvm")) return "pu"; // Purpur Plugin
        if (filePath.endsWith(".mcm.jvm")) return "base"; // Mod registry file
        return "base"; // Base JavaAm
    }

    private static void printUsage() {
        System.out.println("Usage: javaam <command> [options]");
        System.out.println("Commands:");
        System.out.println("  compile <file>  Compile a .jvm file to Java");
        System.out.println("  dev [command]   Launch JVM mcmp dev for templates, AI code generation, uploads, and project scaffolding");
        System.out.println("  help            Show this help message");
    }

    private static void printHelp() {
        System.out.println("=== JavaAm Compiler ===");
        System.out.println();
        System.out.println("JavaAm is a simplified Java programming language that compiles to Java.");
        System.out.println();
        System.out.println("File Extensions:");
        System.out.println("  .jvm        - Base JavaAm file");
        System.out.println("  .fm.jvm     - Fabric Mod");
        System.out.println("  .nfm.jvm    - NeoForge Mod");
        System.out.println("  .fom.jvm    - Forge Mod");
        System.out.println("  .pp.jvm     - Paper Plugin");
        System.out.println("  .sp.jvm     - Spigot Plugin");
        System.out.println("  .pu.jvm     - Purpur Plugin");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  javaam compile <file>  - Compile a JavaAm file to Java");
        System.out.println("  javaam dev [command]   - Launch JVM mcmp dev for project scaffolding, templates and AI generation");
        System.out.println();
        System.out.println("For more information, visit: https://github.com/javaam/javaam");
    }
}
