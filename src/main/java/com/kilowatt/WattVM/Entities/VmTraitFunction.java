package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.Chunks.VmChunk;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Функция трэйта
 */
@AllArgsConstructor
@Getter
public class VmTraitFunction {
    private final String name;
    private final int paramsAmount;
    private final VmChunk defaultImpl;
}
