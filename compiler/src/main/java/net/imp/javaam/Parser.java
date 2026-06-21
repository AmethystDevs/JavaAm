package com.javaam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Program parse() {
        skipNewlines();
        String packageName = null;

        if (check(Token.Type.PACKAGE)) {
            advance();
            packageName = parseQualifiedName();
            if (check(Token.Type.SEMICOLON)) {
                advance();
            }
        }

        List<ClassDeclaration> classes = new ArrayList<>();
        skipNewlines();

        while (!isAtEnd()) {
            if (check(Token.Type.CLASS)) {
                classes.add(parseClass());
            } else {
                advance();
            }
            skipNewlines();
        }

        return new Program(packageName, classes, 1);
    }

    private ClassDeclaration parseClass() {
        Token classToken = consume(Token.Type.CLASS, "Expected 'class'");
        Token nameToken = consume(Token.Type.IDENTIFIER, "Expected class name");
        List<Statement> statements = new ArrayList<>();
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.CLASS)) {
            skipNewlines();
            if (isAtEnd() || check(Token.Type.CLASS)) {
                break;
            }

            Statement statement = parseStatement();
            if (statement != null) {
                statements.add(statement);
            } else {
                advance();
            }
        }

        return new ClassDeclaration(nameToken.value, statements, classToken.line);
    }

    private Statement parseStatement() {
        if (check(Token.Type.NEW)) {
            return parseNewDefinition();
        }

        if (check(Token.Type.MATH)) {
            return parseMathBlock();
        }

        if (check(Token.Type.MODTYPE)) {
            return parseModRegistry();
        }

        if (check(Token.Type.REGISTER)) {
            return parseRegisterStatement();
        }

        if (check(Token.Type.PRINT)) {
            return parsePrintStatement();
        }

        if (check(Token.Type.NEWLINE)) {
            advance();
            return null;
        }

        if (check(Token.Type.RETURN)) {
            return parseReturn();
        }

        return null;
    }

    private NewDefinition parseNewDefinition() {
        Token start = advance();
        String objectType;

        if (check(Token.Type.BLOCK)) {
            advance();
            objectType = "block";
        } else if (check(Token.Type.ITEM)) {
            advance();
            objectType = "item";
        } else {
            throw error("Expected 'block' or 'item' after 'new'");
        }

        Token nameToken = consume(Token.Type.IDENTIFIER, "Expected name after new " + objectType);
        String displayName = null;
        if (check(Token.Type.STRING)) {
            displayName = advance().value;
        }

        NewDefinition def = new NewDefinition(objectType, nameToken.value, displayName, start.line);
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.CLASS) && !check(Token.Type.RETURN) && !check(Token.Type.REGISTER)) {
            if (check(Token.Type.PIPE)) {
                parseSection(def);
                skipNewlines();
                continue;
            }

            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            break;
        }

        if (check(Token.Type.RETURN)) {
            parseReturn();
        }

        skipNewlines();
        if (check(Token.Type.REGISTER)) {
            def.registration = parseRegisterStatement();
        }

        return def;
    }

    private void parseSection(NewDefinition def) {
        consume(Token.Type.PIPE, "Expected section start '|'");

        if (check(Token.Type.TYPE)) {
            advance();
            consume(Token.Type.MINUS, "Expected '-' after type");
            def.typeValue = parseSectionContent();
            return;
        }

        if (check(Token.Type.PROPERTIES)) {
            advance();
            consume(Token.Type.MINUS, "Expected '-' after properties");
            parsePropertiesSection(def.properties);
            return;
        }

        if (check(Token.Type.FUNCTION)) {
            advance();
            consume(Token.Type.MINUS, "Expected '-' after function");
            parseFunctionSection(def);
            return;
        }

        skipUntil(Token.Type.DASH_PIPE);
        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private String parseSectionContent() {
        StringBuilder result = new StringBuilder();
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                if (result.length() > 0) {
                    result.append(" ");
                }
                continue;
            }
            result.append(peek().value);
            advance();
            if (!check(Token.Type.NEWLINE) && !check(Token.Type.DASH_PIPE)) {
                result.append(" ");
            }
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }

        return result.toString().trim();
    }

    private void parsePropertiesSection(Map<String, String> properties) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            if (check(Token.Type.STRING)) {
                String key = advance().value;
                consume(Token.Type.COLON, "Expected ':' in property");
                String value = parseValueToken();
                properties.put(key, value);
                continue;
            }

            advance();
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private void parseFunctionSection(NewDefinition def) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            if (check(Token.Type.PIPE)) {
                advance();
                if (check(Token.Type.NEW) && peekToken(1).type == Token.Type.DOT) {
                    advance();
                    advance(); // DOT
                    String functionName = consume(Token.Type.IDENTIFIER, "Expected function identifier").value;
                    consume(Token.Type.MINUS, "Expected '-' after function section");
                    FunctionSection function = new FunctionSection(functionName, functionName.hashCode());
                    parseFunctionBody(function);
                    def.functions.put(functionName, function);
                    continue;
                }
            }

            advance();
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private void parseFunctionBody(FunctionSection function) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            if (check(Token.Type.STRING)) {
                String key = advance().value;
                consume(Token.Type.COLON, "Expected ':' in function mapping");
                String value = parseValueToken();
                function.actions.put(key, value);
                continue;
            }

            advance();
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private MathBlock parseMathBlock() {
        Token start = advance();
        MathBlock math = new MathBlock("math", start.line);
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.RETURN) && !check(Token.Type.CLASS) && !check(Token.Type.NEW)) {
            if (check(Token.Type.PIPE)) {
                advance();
                if (check(Token.Type.VAR)) {
                    advance();
                    consume(Token.Type.MINUS, "Expected '-' after var");
                    parseMathVarSection(math);
                    continue;
                }
                if (check(Token.Type.SOLVE)) {
                    advance();
                    consume(Token.Type.MINUS, "Expected '-' after solve");
                    parseMathSolveSection(math);
                    continue;
                }
                if (check(Token.Type.PRINT)) {
                    advance();
                    consume(Token.Type.MINUS, "Expected '-' after print");
                    parseMathPrintSection(math);
                    continue;
                }
            }
            advance();
        }

        if (check(Token.Type.RETURN)) {
            parseReturn();
        }

        return math;
    }

    private void parseMathVarSection(MathBlock math) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            if (check(Token.Type.IDENTIFIER)) {
                String name = advance().value;
                consume(Token.Type.ASSIGN, "Expected '=' in variable assignment");
                String value = parseValueToken();
                math.vars.add(new VariableAssignment(name, value, line()));
                continue;
            }

            advance();
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private void parseMathSolveSection(MathBlock math) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            String expression = parseLineTextUntil(Token.Type.NEWLINE, Token.Type.DASH_PIPE);
            if (!expression.isEmpty()) {
                math.solves.add(expression);
            }
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private void parseMathPrintSection(MathBlock math) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }

            String expression = parseLineTextUntil(Token.Type.NEWLINE, Token.Type.DASH_PIPE);
            if (!expression.isEmpty()) {
                math.prints.add(expression);
            }
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private ModRegistryBlock parseModRegistry() {
        Token start = advance();
        ModRegistryBlock mod = new ModRegistryBlock("modReg", start.line);

        if (check(Token.Type.DOT)) {
            advance();
            mod.modLoaders = consume(Token.Type.IDENTIFIER, "Expected mod loader").value;
            while (check(Token.Type.MINUS)) {
                advance();
                mod.modLoaders += "-" + consume(Token.Type.IDENTIFIER, "Expected additional mod loader").value;
            }
        }

        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.RETURN) && !check(Token.Type.CLASS) && !check(Token.Type.NEW)) {
            if (check(Token.Type.MODID)) {
                advance();
                consume(Token.Type.ASSIGN, "Expected '=' after MOD_ID");
                mod.modId = parseValueToken();
                continue;
            }

            if (check(Token.Type.LOGGER)) {
                advance();
                consume(Token.Type.ASSIGN, "Expected '=' after LOGGER");
                mod.logger = parseLineTextUntil(Token.Type.NEWLINE, Token.Type.DASH_PIPE);
                continue;
            }

            if (check(Token.Type.PIPE)) {
                advance();
                if (check(Token.Type.REGLOG)) {
                    advance();
                    consume(Token.Type.MINUS, "Expected '-' after regLog");
                    parseRegLogSection(mod);
                    continue;
                }
            }

            advance();
        }

        if (check(Token.Type.RETURN)) {
            parseReturn();
        }

        return mod;
    }

    private void parseRegLogSection(ModRegistryBlock mod) {
        skipNewlines();

        while (!isAtEnd() && !check(Token.Type.DASH_PIPE)) {
            if (check(Token.Type.NEWLINE)) {
                advance();
                continue;
            }
            if (check(Token.Type.STRING)) {
                mod.regLogs.add(advance().value);
                continue;
            }
            advance();
        }

        if (check(Token.Type.DASH_PIPE)) {
            advance();
        }
    }

    private RegisterStatement parseRegisterStatement() {
        Token start = advance();
        boolean isBlock = false;
        boolean isItem = false;

        if (check(Token.Type.BLOCK)) {
            advance();
            isBlock = true;
        } else if (check(Token.Type.ITEM)) {
            advance();
            isItem = true;
        } else {
            throw error("Expected 'block' or 'item' after register");
        }

        consume(Token.Type.IN, "Expected 'in'");
        consume(Token.Type.GROUP, "Expected 'group'");
        String groupName = consume(Token.Type.IDENTIFIER, "Expected group name").value;
        skipNewlines();
        String targetName = null;

        if (check(Token.Type.PIPE)) {
            advance();
            if (check(Token.Type.IDENTIFIER)) {
                targetName = advance().value;
            }
        }

        while (!isAtEnd() && !check(Token.Type.RETURN) && !check(Token.Type.CLASS) && !check(Token.Type.NEW)) {
            if (check(Token.Type.NEWLINE) || check(Token.Type.SEMICOLON)) {
                advance();
                continue;
            }
            advance();
        }

        if (check(Token.Type.RETURN)) {
            parseReturn();
        }

        return new RegisterStatement(isBlock ? "block" : "item", groupName, targetName, start.line);
    }

    private PrintStatement parsePrintStatement() {
        Token start = advance();
        String expression = parseLineTextUntil(Token.Type.NEWLINE, Token.Type.SEMICOLON, Token.Type.DASH_PIPE);
        if (check(Token.Type.SEMICOLON)) {
            advance();
        }
        return new PrintStatement(expression, start.line);
    }

    private ReturnStatement parseReturn() {
        Token start = advance();
        String value = null;
        if (!check(Token.Type.SEMICOLON) && !check(Token.Type.NEWLINE) && !isAtEnd()) {
            value = parseValueToken();
        }
        if (check(Token.Type.SEMICOLON)) {
            advance();
        }
        return new ReturnStatement(value, start.line);
    }

    private String parseValueToken() {
        if (check(Token.Type.STRING)) {
            return advance().value;
        }

        return parseLineTextUntil(Token.Type.NEWLINE, Token.Type.SEMICOLON, Token.Type.DASH_PIPE);
    }

    private String parseQualifiedName() {
        StringBuilder result = new StringBuilder();
        result.append(consume(Token.Type.IDENTIFIER, "Expected package identifier").value);
        while (check(Token.Type.DOT)) {
            advance();
            result.append('.');
            result.append(consume(Token.Type.IDENTIFIER, "Expected package identifier after '.'").value);
        }
        return result.toString();
    }

    private String parseLineTextUntil(Token.Type... endTypes) {
        Set<Token.Type> stopTypes = new HashSet<>(Arrays.asList(endTypes));
        StringBuilder result = new StringBuilder();

        while (!isAtEnd() && !stopTypes.contains(peek().type)) {
            if (check(Token.Type.NEWLINE)) {
                break;
            }
            result.append(peek().value);
            advance();
            if (!stopTypes.contains(peek().type) && !check(Token.Type.NEWLINE)) {
                result.append(" ");
            }
        }

        while (!isAtEnd() && check(Token.Type.NEWLINE)) {
            advance();
        }

        return result.toString().trim();
    }

    private void skipUntil(Token.Type type) {
        while (!isAtEnd() && !check(type)) {
            advance();
        }
    }

    private void skipNewlines() {
        while (check(Token.Type.NEWLINE)) {
            advance();
        }
    }

    private Token peekToken(int offset) {
        int index = pos + offset;
        if (index >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(index);
    }

    private boolean check(Token.Type type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) pos++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == Token.Type.EOF;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    private Token consume(Token.Type type, String message) {
        if (check(type)) return advance();
        throw error(message + " at " + peek());
    }

    private RuntimeException error(String message) {
        return new RuntimeException(message + " at " + peek());
    }

    private int line() {
        return peek().line;
    }
}
