package com.kilowatt.WattVM.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Выкидываемое
 */
@AllArgsConstructor
@Getter
public class VmThrowable extends RuntimeException {
    // значение
    private final Object value;

    // в строку
    @Override
    public String toString() {
        return "VmThrowable{" +
                "value=" + value +
                '}';
    }

    // получение значения
    public Object value() {
        return this.value;
    }
}
