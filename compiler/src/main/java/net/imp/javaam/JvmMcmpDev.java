package com.javaam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JvmMcmpDev {
    public static void main(String[] args) {
        try {
            run(args);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void run(String[] args) throws IOException {
        JvmMcmpDev tool = new JvmMcmpDev();
        tool.execute(args);
    }

    private void execute(String[] args) throws IOException {
        if (args.length == 0) {
            showInteractiveMenu();
            return;
        }

        String command = args[0];
        switch (command) {
            case "help":
                printHelp();
                break;
            case "list-templates":
                listTemplates();
                break;
            case "init":
                initTemplate(args);
                break;
            case "ai":
                aiGenerate(args);
                break;
            case "upload":
                uploadFile(args);
                break;
            default:
                System.out.println("Unknown dev command: " + command);
                printHelp();
                break;
        }
    }

    private void showInteractiveMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== JVM mcmp dev ===");
        System.out.println("1) List available templates");
        System.out.println("2) Create a project from a template");
        System.out.println("3) Generate a JavaAm file with AI prompt");
        System.out.println("4) Upload a file into the workspace");
        System.out.println("5) Show help");
        System.out.print("Choose an option [1-5]: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                listTemplates();
                break;
            case "2":
                System.out.print("Template name: ");
                String templateName = scanner.nextLine().trim();
                System.out.print("Target folder (optional): ");
                String target = scanner.nextLine().trim();
                execute(new String[]{"init", templateName, target.isEmpty() ? "" : target});
                break;
            case "3":
                System.out.print("Describe the mod or plugin you want to generate: ");
                String prompt = scanner.nextLine().trim();
                System.out.print("Output file path (optional): ");
                String output = scanner.nextLine().trim();
                if (output.isEmpty()) {
                    execute(new String[]{"ai", prompt});
                } else {
                    execute(new String[]{"ai", prompt, output});
                }
                break;
            case "4":
                System.out.print("Source file path: ");
                String source = scanner.nextLine().trim();
                System.out.print("Destination folder (optional): ");
                String destination = scanner.nextLine().trim();
                if (destination.isEmpty()) {
                    execute(new String[]{"upload", source});
                } else {
                    execute(new String[]{"upload", source, destination});
                }
                break;
            default:
                printHelp();
                break;
        }
    }

    private void printHelp() {
        System.out.println("JavaAm MC mods and plugins dev (JVM mcmp dev)");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  help                   Show this help message");
        System.out.println("  list-templates         List available project templates");
        System.out.println("  init <template> [dir]  Create a project from a template");
        System.out.println("  ai <prompt> [file]     Generate a JavaAm file from a prompt");
        System.out.println("  upload <source> [dir]  Copy a local file into the workspace");
        System.out.println();
        System.out.println("Templates are loaded from the top-level templates/ folder in the JavaAm repository.");
    }

    private void listTemplates() throws IOException {
        Path root = findWorkspaceRoot();
        Path templates = root.resolve("templates");
        if (!Files.exists(templates) || !Files.isDirectory(templates)) {
            System.out.println("No templates folder found at " + templates);
            return;
        }

        System.out.println("Available templates:");
        try {
            Files.list(templates)
                .filter(Files::isDirectory)
                .sorted()
                .forEach(path -> System.out.println("  - " + path.getFileName()));
        } catch (IOException e) {
            System.err.println("Failed to list templates: " + e.getMessage());
        }
    }

    private void initTemplate(String[] args) throws IOException {
        if (args.length < 2 || args[1].trim().isEmpty()) {
            System.err.println("Template name is required. Use 'list-templates' to view available template names.");
            return;
        }

        String templateName = args[1].trim();
        Path root = findWorkspaceRoot();
        Path templateFolder = root.resolve("templates").resolve(templateName);
        if (!Files.exists(templateFolder) || !Files.isDirectory(templateFolder)) {
            System.err.println("Template not found: " + templateName);
            return;
        }

        Path outputFolder;
        if (args.length >= 3 && !args[2].trim().isEmpty()) {
            outputFolder = Paths.get(args[2]).toAbsolutePath();
        } else {
            outputFolder = root.resolve(templateName + "-project");
        }

        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }

        copyDirectory(templateFolder, outputFolder);
        Path readme = outputFolder.resolve("README.md");
        Files.write(readme, Collections.singletonList("# " + templateName + " project\n\nGenerated by JVM mcmp dev."), StandardCharsets.UTF_8);
        System.out.println("Created project from template '" + templateName + "' at " + outputFolder);
    }

    private void aiGenerate(String[] args) throws IOException {
        if (args.length < 2 || args[1].trim().isEmpty()) {
            System.err.println("AI prompt is required.");
            return;
        }

        String prompt = args[1].trim();
        String outputFileName = args.length >= 3 && !args[2].trim().isEmpty() ? args[2].trim() : "jvm-mcmp-dev-generated.jvm";
        Path outputPath = Paths.get(outputFileName);
        if (Files.isDirectory(outputPath)) {
            outputPath = outputPath.resolve("generated.jvm");
        }

        String content = generateJavaAmFromPrompt(prompt);
        if (!Files.exists(outputPath.getParent())) {
            Files.createDirectories(outputPath.getParent());
        }
        Files.write(outputPath, content.getBytes(StandardCharsets.UTF_8));
        System.out.println("Generated JavaAm file at " + outputPath.toAbsolutePath());
    }

    private void uploadFile(String[] args) throws IOException {
        if (args.length < 2 || args[1].trim().isEmpty()) {
            System.err.println("Source file path is required.");
            return;
        }

        Path source = Paths.get(args[1]).toAbsolutePath();
        if (!Files.exists(source)) {
            System.err.println("Source file not found: " + source);
            return;
        }

        Path targetFolder;
        if (args.length >= 3 && !args[2].trim().isEmpty()) {
            targetFolder = Paths.get(args[2]).toAbsolutePath();
        } else {
            targetFolder = findWorkspaceRoot();
        }

        if (!Files.exists(targetFolder)) {
            Files.createDirectories(targetFolder);
        }

        Path destination = targetFolder.resolve(source.getFileName());
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Uploaded " + source + " to " + destination);
    }

    private String generateJavaAmFromPrompt(String prompt) {
        String lowercase = prompt.toLowerCase();
        if (lowercase.contains("plugin") || lowercase.contains("spigot") || lowercase.contains("paper")) {
            return "package com.example;\n\n" +
                "class GeneratedPlugin\n" +
                "    new item ExampleItem \"example_item\"\n" +
                "        |properties-\n" +
                "            \"durability\": 250\n" +
                "        -|\n" +
                "    return item;\n\n" +
                "    register item in group TOOLS\n" +
                "        |ExampleItem\n" +
                "    return regitem\n" +
                "return class;\n";
        }

        if (lowercase.contains("forge") || lowercase.contains("fabric") || lowercase.contains("mod")) {
            return "package com.example;\n\n" +
                "class GeneratedMod\n" +
                "    new block ExampleBlock \"example_block\"\n" +
                "        |type-\n" +
                "            stone\n" +
                "        -|\n" +
                "        |properties-\n" +
                "            \"hardness\": 1.5\n" +
                "            \"sound\": \"stone\"\n" +
                "        -|\n" +
                "    return block;\n\n" +
                "    register block in group BUILDING\n" +
                "        |ExampleBlock\n" +
                "    return regblock\n" +
                "return class;\n";
        }

        return "package com.example;\n\n" +
            "class GeneratedProject\n" +
            "    new block ExampleBlock \"example_block\"\n" +
            "        |type-\n" +
            "            stone\n" +
            "        -|\n" +
            "    return block;\n\n" +
            "    register block in group BUILDING\n" +
            "        |ExampleBlock\n" +
            "    return regblock\n" +
            "return class;\n";
    }

    private void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectory(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private Path findWorkspaceRoot() {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("compiler")) && Files.exists(current.resolve("javaam-vscode"))) {
                return current;
            }
            if (Files.exists(current.resolve("templates"))) {
                return current;
            }
            current = current.getParent();
        }
        return Paths.get(System.getProperty("user.dir")).toAbsolutePath();
    }
}
