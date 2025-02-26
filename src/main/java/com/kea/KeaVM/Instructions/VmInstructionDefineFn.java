package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Entities.VmFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
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
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        frame.define(addr, fn.getName(), fn);
    }

    @Override
    public String toString() {
        return "DEFINE_FUNC(" + fn.getName() + "," + fn.getInstructions() + ")";
    }
}
