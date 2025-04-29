package com.kilowatt.WattVM.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Брик лупа
 */
@AllArgsConstructor
@Getter
public class VmLoopBreak extends RuntimeException {
    private final boolean currentIteration;
}
