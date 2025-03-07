package com.kilowatt.Lexer;

import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;

/*
Токены - на них делится текст.
 */
@Getter
public class Token {
    // тип токена
    public final TokenType type;
    // значение токена
    public final String value;
    // файл адреса токена
    public final String fileName;
    // линия адреса токена
    public final int line;

    // конструктор
    public Token(TokenType type, String value, int line, String fileName) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.fileName = fileName;
    }

    // в строку

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", file=" + fileName +
                '}';
    }

    // как адрес
    public VmAddress asAddress() {
        return new VmAddress(fileName, line);
    }
}
