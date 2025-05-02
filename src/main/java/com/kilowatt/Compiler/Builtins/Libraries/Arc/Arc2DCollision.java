package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.kilowatt.WattVM.Entities.VmFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Столкновение
 */
@Getter
@AllArgsConstructor
public class Arc2DCollision {
    private final Arc2DSprite first;
    private final Arc2DSprite second;
    private final VmFunction fn;
}
