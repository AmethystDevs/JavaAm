package com.javaam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ASTNode {
    public int line;

    public ASTNode(int line) {
        this.line = line;
    }
}

class Program extends ASTNode {
    public String packageName;
    public List<ClassDeclaration> classes;

    public Program(String packageName, List<ClassDeclaration> classes, int line) {
        super(line);
        this.packageName = packageName;
        this.classes = classes;
    }
}

class ClassDeclaration extends ASTNode {
    public String name;
    public List<Statement> statements;

    public ClassDeclaration(String name, List<Statement> statements, int line) {
        super(line);
        this.name = name;
        this.statements = statements;
    }
}

abstract class Statement extends ASTNode {
    public Statement(int line) {
        super(line);
    }
}

class NewDefinition extends Statement {
    public String objectType;
    public String name;
    public String displayName;
    public String typeValue;
    public Map<String, String> properties = new LinkedHashMap<>();
    public Map<String, FunctionSection> functions = new LinkedHashMap<>();
    public RegisterStatement registration;

    public NewDefinition(String objectType, String name, String displayName, int line) {
        super(line);
        this.objectType = objectType;
        this.name = name;
        this.displayName = displayName;
    }
}

class RegisterStatement extends Statement {
    public String objectType;
    public String group;
    public String targetName;

    public RegisterStatement(String objectType, String group, String targetName, int line) {
        super(line);
        this.objectType = objectType;
        this.group = group;
        this.targetName = targetName;
    }
}

class MathBlock extends Statement {
    public List<VariableAssignment> vars = new ArrayList<>();
    public List<String> solves = new ArrayList<>();
    public List<String> prints = new ArrayList<>();

    public MathBlock(String name, int line) {
        super(line);
    }
}

class ModRegistryBlock extends Statement {
    public String modLoaders = "";
    public String modId;
    public String logger;
    public List<String> regLogs = new ArrayList<>();

    public ModRegistryBlock(String name, int line) {
        super(line);
    }
}

class PrintStatement extends Statement {
    public String expression;

    public PrintStatement(String expression, int line) {
        super(line);
        this.expression = expression;
    }
}

class ReturnStatement extends Statement {
    public String value;

    public ReturnStatement(String value, int line) {
        super(line);
        this.value = value;
    }
}

class FunctionSection extends ASTNode {
    public String name;
    public Map<String, String> actions = new LinkedHashMap<>();

    public FunctionSection(String name, int line) {
        super(line);
        this.name = name;
    }
}

class VariableAssignment extends ASTNode {
    public String name;
    public String value;

    public VariableAssignment(String name, String value, int line) {
        super(line);
        this.name = name;
        this.value = value;
    }
}
