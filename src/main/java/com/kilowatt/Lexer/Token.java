package com.kilowatt.Lexer;

import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
Токены - на них делится текст.
 */
@Getter
@AllArgsConstructor
public class Token {
    // тип токена
    public final TokenType type;
    // значение токена
    @Setter
    public String value;
    // адрес
    public final int line;
    public final int column;
    public final String fileName;
    private final String lineText;

    // в строку
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", column=" + column +
                ", file=" + fileName +
                '}';
    }

    // как адрес
    public VmAddress asAddress() {
        return new VmAddress(fileName, line, column, lineText);
    }
}
