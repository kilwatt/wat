package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Определение функции
 */
@Getter
public class VmInstructionDefineFn implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // юнит
    private final VmFunction fn;

    // конструктор
    public VmInstructionDefineFn(VmAddress addr, VmFunction fn) {
        this.addr = addr;
        this.fn = fn;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        frame.define(addr, fn.getName(), fn.copy());
    }

    @Override
    public String toString() {
        return "DEFINE_FUNC(" + fn.getName() + "," + fn.getInstructions() + ")";
    }
}
