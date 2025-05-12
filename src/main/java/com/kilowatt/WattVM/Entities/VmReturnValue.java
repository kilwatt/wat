package com.kilowatt.WattVM.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Возвращаемое значение
 */
@AllArgsConstructor
@Getter
public class VmReturnValue extends RuntimeException {
    // объект
    private final Object object;

    // заполнение стак трейса
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}