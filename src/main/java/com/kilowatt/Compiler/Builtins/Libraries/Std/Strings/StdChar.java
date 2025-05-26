package com.kilowatt.Compiler.Builtins.Libraries.Std.Strings;

/*
Стд -> Символы
 */
public class StdChar {
    // цифра ли это
    public boolean is_digit(char c) {
        return c >= '0' && c <= '9';
    }

    // буква или нижнее подчёркивание ли это
    public boolean is_alpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }
}
