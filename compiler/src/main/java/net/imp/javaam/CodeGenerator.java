package com.javaam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    private final StringBuilder output = new StringBuilder();
    private int indentLevel = 0;
    private final String modType;
    private int printCounter = 1;
    private int mathCounter = 1;
    private int registerCounter = 1;

    public CodeGenerator(String modType) {
        this.modType = modType;
    }

    public String generate(Program program) {
        if (program.packageName != null) {
            emit("package " + program.packageName + ";");
            newLine();
            newLine();
        }

        for (ClassDeclaration cls : program.classes) {
            generateClass(cls);
            newLine();
        }

        return output.toString();
    }

    private void generateClass(ClassDeclaration cls) {
        emit("public class " + cls.name + " {");
        newLine();
        indent();

        List<String> initCalls = new ArrayList<>();

        for (Statement stmt : cls.statements) {
            String call = generateStatement(stmt);
            if (call != null) {
                initCalls.add(call);
            }
            newLine();
        }

        emit("public void init() {");
        newLine();
        indent();
        for (String call : initCalls) {
            emit(call + "();");
            newLine();
        }
        dedent();
        emit("}");
        newLine();
        emit("public static void main(String[] args) {");
        newLine();
        indent();
        emit("new " + cls.name + "().init();");
        newLine();
        dedent();
        emit("}");
        newLine();
        emitHelperClasses();
        newLine();

        dedent();
        emit("}");
    }

    private String generateStatement(Statement stmt) {
        if (stmt instanceof NewDefinition) {
            return generateNewDefinition((NewDefinition) stmt);
        }
        if (stmt instanceof RegisterStatement) {
            return generateRegisterStatement((RegisterStatement) stmt);
        }
        if (stmt instanceof MathBlock) {
            return generateMathBlock((MathBlock) stmt);
        }
        if (stmt instanceof ModRegistryBlock) {
            return generateModRegistry((ModRegistryBlock) stmt);
        }
        if (stmt instanceof PrintStatement) {
            return generatePrintStatement((PrintStatement) stmt);
        }
        if (stmt instanceof ReturnStatement) {
            return generateReturnStatement((ReturnStatement) stmt);
        }
        return null;
    }

    private String generateNewDefinition(NewDefinition def) {
        String fieldName = sanitizeIdentifier(def.name);
        String typeName = def.objectType.equals("item") ? "Item" : "Block";
        emit("public static " + typeName + " " + fieldName + ";");
        newLine();
        emit("private void register" + capitalize(fieldName) + "() {");
        newLine();
        indent();
        emit(fieldName + " = new " + typeName + "(\"" + def.name + "\");");
        newLine();
        if (def.displayName != null) {
            emit(fieldName + ".displayName = \"" + def.displayName + "\";");
            newLine();
        }
        if (def.typeValue != null && !def.typeValue.isEmpty()) {
            emit(fieldName + ".type = \"" + def.typeValue + "\";");
            newLine();
        }
        for (Map.Entry<String, String> entry : def.properties.entrySet()) {
            emit(fieldName + ".properties.put(\"" + entry.getKey() + "\", \"" + entry.getValue() + "\");");
            newLine();
        }
        for (FunctionSection function : def.functions.values()) {
            emit("FunctionDefinition " + sanitizeIdentifier(function.name) + "Fn = new FunctionDefinition(\"" + function.name + "\");");
            newLine();
            for (Map.Entry<String, String> entry : function.actions.entrySet()) {
                emit(sanitizeIdentifier(function.name) + "Fn.actions.put(\"" + entry.getKey() + "\", \"" + entry.getValue() + "\");");
                newLine();
            }
            emit(fieldName + ".functions.put(" + sanitizeIdentifier(function.name) + "Fn.name, " + sanitizeIdentifier(function.name) + "Fn);");
            newLine();
        }
        if (def.registration != null) {
            String target = def.registration.targetName != null ? sanitizeIdentifier(def.registration.targetName) : fieldName;
            emit("registerInGroup(\"" + def.objectType + "\", \"" + def.registration.group + "\", " + target + ");");
            newLine();
        }
        dedent();
        emit("}");
        return "register" + capitalize(fieldName);
    }

    private String generateRegisterStatement(RegisterStatement stmt) {
        String methodName = "register" + capitalize(stmt.objectType) + registerCounter++;
        emit("private void " + methodName + "() {");
        newLine();
        indent();
        if (stmt.targetName != null) {
            emit("registerInGroup(\"" + stmt.objectType + "\", \"" + stmt.group + "\", " + sanitizeIdentifier(stmt.targetName) + ");");
        } else {
            emit("registerInGroup(\"" + stmt.objectType + "\", \"" + stmt.group + "\", null);");
        }
        newLine();
        dedent();
        emit("}");
        return methodName;
    }

    private String generateMathBlock(MathBlock math) {
        String methodName = "executeMath" + mathCounter++;
        emit("private void " + methodName + "() {");
        newLine();
        indent();
        for (VariableAssignment variable : math.vars) {
            String value = variable.value != null && !variable.value.isEmpty() ? variable.value : "0";
            emit("double " + variable.name + " = " + value + ";");
            newLine();
        }
        for (int i = 0; i < math.solves.size(); i++) {
            String expression = math.solves.get(i);
            emit("double sol" + (i + 1) + " = " + expression + ";");
            newLine();
        }
        for (String printValue : math.prints) {
            emit("System.out.println(" + printValue + ");");
            newLine();
        }
        dedent();
        emit("}");
        return methodName;
    }

    private String generateModRegistry(ModRegistryBlock mod) {
        String methodName = "initModRegistry";
        emit("public static final String MOD_ID = \"" + (mod.modId != null ? mod.modId : "UNKNOWN_MOD_ID") + "\";");
        newLine();
        if (mod.logger != null && !mod.logger.isEmpty()) {
            emit("public static final String LOGGER = \"" + mod.logger + "\";");
            newLine();
        }
        emit("private void " + methodName + "() {");
        newLine();
        indent();
        if (mod.modLoaders != null && !mod.modLoaders.isEmpty()) {
            emit("System.out.println(\"Using mod loaders: " + mod.modLoaders + "\");");
            newLine();
        }
        for (String log : mod.regLogs) {
            emit("System.out.println(\"Mod registry log: " + log + "\");");
            newLine();
        }
        dedent();
        emit("}");
        return methodName;
    }

    private String generatePrintStatement(PrintStatement stmt) {
        String methodName = "printMessage" + printCounter++;
        emit("private void " + methodName + "() {");
        newLine();
        indent();
        emit("System.out.println(" + stmt.expression + ");");
        newLine();
        dedent();
        emit("}");
        return methodName;
    }

    private String generateReturnStatement(ReturnStatement stmt) {
        String methodName = "returnStatement" + printCounter++;
        emit("private void " + methodName + "() {");
        newLine();
        indent();
        if (stmt.value != null && !stmt.value.isEmpty()) {
            emit("// return " + stmt.value);
        } else {
            emit("// return;");
        }
        newLine();
        dedent();
        emit("}");
        return methodName;
    }

    private void emit(String str) {
        output.append(getIndent()).append(str);
    }

    private void newLine() {
        output.append("\n");
    }

    private void indent() {
        indentLevel++;
    }

    private void dedent() {
        indentLevel = Math.max(0, indentLevel - 1);
    }

    private String getIndent() {
        return "    ".repeat(Math.max(0, indentLevel));
    }

    private String sanitizeIdentifier(String value) {
        return value.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private void emitHelperClasses() {
        emit("private void registerInGroup(String objectType, String group, GameObject target) {");
        newLine();
        indent();
        emit("if (target == null) {");
        newLine();
        indent();
        emit("System.out.println(\"Registering \" + objectType + \" in group \" + group + \": <null target>\");");
        newLine();
        dedent();
        emit("} else {");
        newLine();
        indent();
        emit("System.out.println(\"Registering \" + objectType + \" in group \" + group + \": \" + target.name);");
        newLine();
        dedent();
        emit("}");
        newLine();
        dedent();
        emit("}");
        newLine();
        emit("private static abstract class GameObject {");
        newLine();
        indent();
        emit("public final String name;");
        newLine();
        emit("public String displayName;");
        newLine();
        emit("public String type;");
        newLine();
        emit("public final java.util.Map<String, String> properties = new java.util.LinkedHashMap<>();");
        newLine();
        emit("public final java.util.Map<String, FunctionDefinition> functions = new java.util.LinkedHashMap<>();");
        newLine();
        emit("public GameObject(String name) {");
        newLine();
        indent();
        emit("this.name = name;");
        newLine();
        dedent();
        emit("}");
        newLine();
        dedent();
        emit("}");
        newLine();
        emit("private static class Block extends GameObject {");
        newLine();
        indent();
        emit("public Block(String name) { super(name); }");
        newLine();
        dedent();
        emit("}");
        newLine();
        emit("private static class Item extends GameObject {");
        newLine();
        indent();
        emit("public Item(String name) { super(name); }");
        newLine();
        dedent();
        emit("}");
        newLine();
        emit("private static class FunctionDefinition {");
        newLine();
        indent();
        emit("public final String name;");
        newLine();
        emit("public final java.util.Map<String, String> actions = new java.util.LinkedHashMap<>();");
        newLine();
        emit("public FunctionDefinition(String name) {");
        newLine();
        indent();
        emit("this.name = name;");
        newLine();
        dedent();
        emit("}");
        newLine();
        emit("}");
        newLine();
        dedent();
    }
}
