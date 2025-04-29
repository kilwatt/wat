package com.kilowatt.WattVM.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Возвращаемое значение
 */
@AllArgsConstructor
@Getter
public class VmReturnValue extends RuntimeException {
    private final Object object;
}
