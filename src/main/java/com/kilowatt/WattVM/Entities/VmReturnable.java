package com.kilowatt.WattVM.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Возвращаемое значение
 */
@AllArgsConstructor
@Getter
public class VmReturnable extends RuntimeException {
    private final Object object;
}
