package com.javaam;

public class Token {
    public enum Type {
        // Literals
        NUMBER, STRING, IDENTIFIER,

        // Keywords
        CLASS, PACKAGE, RETURN,
        NEW, BLOCK, ITEM, REGISTER, GROUP, TYPE, PROPERTIES, FUNCTION,
        MATH, VAR, SOLVE, PRINT, MODTYPE, MODID, LOGGER, REGLOG,
        IN,

        // Block Delimiters
        PIPE, DASH, PIPE_DASH, DASH_PIPE,

        // Operators
        PLUS, MINUS, MUL, DIV, MOD, EQ, NE, LT, GT, LE, GE,
        ASSIGN,

        // Other Delimiters
        LPAREN, RPAREN, COLON, COMMA, DOT, SEMICOLON,

        // Special
        EOF, NEWLINE
    }

    public Type type;
    public String value;
    public int line;
    public int column;

    public Token(Type type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, \"%s\", L%d:C%d)", type, value, line, column);
    }
}
