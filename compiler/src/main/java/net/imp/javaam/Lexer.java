package com.javaam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private String input;
    private int pos = 0;
    private int line = 1;
    private int column = 1;
    private List<Token> tokens = new ArrayList<>();

    private static final Map<String, Token.Type> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("class", Token.Type.CLASS);
        KEYWORDS.put("package", Token.Type.PACKAGE);
        KEYWORDS.put("return", Token.Type.RETURN);
        KEYWORDS.put("new", Token.Type.NEW);
        KEYWORDS.put("block", Token.Type.BLOCK);
        KEYWORDS.put("item", Token.Type.ITEM);
        KEYWORDS.put("register", Token.Type.REGISTER);
        KEYWORDS.put("group", Token.Type.GROUP);
        KEYWORDS.put("type", Token.Type.TYPE);
        KEYWORDS.put("properties", Token.Type.PROPERTIES);
        KEYWORDS.put("function", Token.Type.FUNCTION);
        KEYWORDS.put("math", Token.Type.MATH);
        KEYWORDS.put("var", Token.Type.VAR);
        KEYWORDS.put("solve", Token.Type.SOLVE);
        KEYWORDS.put("print", Token.Type.PRINT);
        KEYWORDS.put("modReg", Token.Type.MODTYPE);
        KEYWORDS.put("MOD_ID", Token.Type.MODID);
        KEYWORDS.put("LOGGER", Token.Type.LOGGER);
        KEYWORDS.put("regLog", Token.Type.REGLOG);
        KEYWORDS.put("in", Token.Type.IN);
    }

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        while (pos < input.length()) {
            char current = input.charAt(pos);

            if (current == '\uFEFF') {
                pos++;
                continue;
            }

            if (current == '\r') {
                pos++;
                continue;
            }

            if (current == '\n') {
                tokens.add(new Token(Token.Type.NEWLINE, "\\n", line, column));
                pos++;
                line++;
                column = 1;
                continue;
            }

            if (Character.isWhitespace(current)) {
                pos++;
                column++;
                continue;
            }

            // Comments
            if (current == '/' && peek() == '/') {
                skipLineComment();
                continue;
            }

            int tokenLine = line;
            int tokenColumn = column;

            // String literals
            if (current == '"') {
                tokens.add(new Token(Token.Type.STRING, scanString(), tokenLine, tokenColumn));
                continue;
            }

            // Numbers
            if (Character.isDigit(current)) {
                tokens.add(new Token(Token.Type.NUMBER, scanNumber(), tokenLine, tokenColumn));
                continue;
            }

            // Block markers: |- and -|
            if (current == '|' && peek() == '-') {
                tokens.add(new Token(Token.Type.PIPE_DASH, "|-", tokenLine, tokenColumn));
                pos += 2;
                column += 2;
                continue;
            }

            if (current == '-' && peek() == '|') {
                tokens.add(new Token(Token.Type.DASH_PIPE, "-|", tokenLine, tokenColumn));
                pos += 2;
                column += 2;
                continue;
            }

            // Identifiers and keywords
            if (Character.isLetter(current) || current == '_') {
                String ident = scanIdentifier();
                Token.Type type = KEYWORDS.getOrDefault(ident, Token.Type.IDENTIFIER);
                tokens.add(new Token(type, ident, tokenLine, tokenColumn));
                continue;
            }

            // Operators and delimiters
            if (scanOperator()) continue;

            // Unknown character
            throw new RuntimeException(String.format("Unexpected character '%c' at L%d:C%d", current, line, column));
        }

        tokens.add(new Token(Token.Type.EOF, "", line, column));
        return tokens;
    }

    private void skipLineComment() {
        while (pos < input.length() && input.charAt(pos) != '\n') {
            pos++;
            column++;
        }
    }

    private String scanString() {
        pos++; // Skip opening "
        column++;
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && input.charAt(pos) != '"') {
            if (input.charAt(pos) == '\\' && pos + 1 < input.length()) {
                pos++;
                column++;
                sb.append(input.charAt(pos));
            } else {
                sb.append(input.charAt(pos));
            }
            pos++;
            column++;
        }
        if (pos < input.length()) {
            pos++; // Skip closing "
            column++;
        }
        return sb.toString();
    }

    private String scanNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            sb.append(input.charAt(pos));
            pos++;
            column++;
        }
        return sb.toString();
    }

    private String scanIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
            sb.append(input.charAt(pos));
            pos++;
            column++;
        }
        return sb.toString();
    }

    private boolean scanOperator() {
        int tokenLine = line;
        int tokenColumn = column;
        char current = input.charAt(pos);

        if (pos + 1 < input.length()) {
            String twoChar = input.substring(pos, pos + 2);
            Token.Type type = null;

            switch (twoChar) {
                case "==": type = Token.Type.EQ; break;
                case "!=": type = Token.Type.NE; break;
                case "<=": type = Token.Type.LE; break;
                case ">=": type = Token.Type.GE; break;
            }

            if (type != null) {
                tokens.add(new Token(type, twoChar, tokenLine, tokenColumn));
                pos += 2;
                column += 2;
                return true;
            }
        }

        switch (current) {
            case '+': tokens.add(new Token(Token.Type.PLUS, "+", tokenLine, tokenColumn)); pos++; column++; return true;
            case '-': tokens.add(new Token(Token.Type.MINUS, "-", tokenLine, tokenColumn)); pos++; column++; return true;
            case '*': tokens.add(new Token(Token.Type.MUL, "*", tokenLine, tokenColumn)); pos++; column++; return true;
            case '/': tokens.add(new Token(Token.Type.DIV, "/", tokenLine, tokenColumn)); pos++; column++; return true;
            case '%': tokens.add(new Token(Token.Type.MOD, "%", tokenLine, tokenColumn)); pos++; column++; return true;
            case '<': tokens.add(new Token(Token.Type.LT, "<", tokenLine, tokenColumn)); pos++; column++; return true;
            case '>': tokens.add(new Token(Token.Type.GT, ">", tokenLine, tokenColumn)); pos++; column++; return true;
            case '=': tokens.add(new Token(Token.Type.ASSIGN, "=", tokenLine, tokenColumn)); pos++; column++; return true;
            case '(' : tokens.add(new Token(Token.Type.LPAREN, "(", tokenLine, tokenColumn)); pos++; column++; return true;
            case ')': tokens.add(new Token(Token.Type.RPAREN, ")", tokenLine, tokenColumn)); pos++; column++; return true;
            case ',': tokens.add(new Token(Token.Type.COMMA, ",", tokenLine, tokenColumn)); pos++; column++; return true;
            case '.': tokens.add(new Token(Token.Type.DOT, ".", tokenLine, tokenColumn)); pos++; column++; return true;
            case ':': tokens.add(new Token(Token.Type.COLON, ":", tokenLine, tokenColumn)); pos++; column++; return true;
            case ';': tokens.add(new Token(Token.Type.SEMICOLON, ";", tokenLine, tokenColumn)); pos++; column++; return true;
            case '|': tokens.add(new Token(Token.Type.PIPE, "|", tokenLine, tokenColumn)); pos++; column++; return true;
            default: return false;
        }
    }

    private char peek() {
        if (pos + 1 >= input.length()) return '\0';
        return input.charAt(pos + 1);
    }
}
