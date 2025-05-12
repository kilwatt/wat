package com.kilowatt.WattVM.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Брик лупа
 */
@AllArgsConstructor
@Getter
public class VmLoopBreak extends RuntimeException {
    // только текущая итерация
    private final boolean currentIteration;

    // заполнение стак трейса
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
