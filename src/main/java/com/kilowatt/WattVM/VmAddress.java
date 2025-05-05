package com.kilowatt.WattVM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Адрес ВМ
 */
@Getter
@AllArgsConstructor
@ToString
public class VmAddress {
    // файл
    private final String fileName;
    // строка
    private final int line;
}
