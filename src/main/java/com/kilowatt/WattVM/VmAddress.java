package com.kilowatt.WattVM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Адрес ВМ
 */
@Getter
@AllArgsConstructor
@ToString(exclude = {"lineText"})
public class VmAddress {
    // файл
    private final String fileName;
    // строка
    private final int line;
    // позиция в строке
    private final int column;
    // строка
    private final String lineText;
}
