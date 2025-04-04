package com.kilowatt.WattVM;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Адрес ВМ
 */
@Getter
@AllArgsConstructor
public class VmAddress {
    // файл
    private final String fileName;

    // строка
    private final int line;

    // в строку
    @Override
    public String toString() {
        return "VmAddress(" +
                "fileName='" + fileName + '\'' +
                ", line=" + line +
                ')';
    }
}
