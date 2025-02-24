package com.kea.Lexer;

/*
Токены - на них делится текст.
 */
public class Token {
    // тип токена
    public final TokenType type;
    // значение токена
    public final String value;
    // линия адресса токена
    public final int line;

    // конструктор
    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    // в строку

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                '}';
    }
}
