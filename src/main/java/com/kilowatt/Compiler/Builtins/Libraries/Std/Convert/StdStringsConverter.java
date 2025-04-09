package com.kilowatt.Compiler.Builtins.Libraries.Std.Convert;


/*
Стд -> Конвертация из строки
 */
public class StdStringsConverter {
    public int parse_int(String value) {
        return Integer.parseInt(value);
    }

    public float parse_float(String value) {
        return Float.parseFloat(value);
    }

    public long parse_long(String value) {
        return Long.parseLong(value);
    }

    public boolean parse_bool(String value) { return Boolean.parseBoolean(value); }
}
