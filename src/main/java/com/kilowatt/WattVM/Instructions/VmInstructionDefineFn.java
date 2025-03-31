package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.VmCodeDumper;
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
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_FN(" + fn.getName() + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : fn.getInstructions()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent + 1, "ARGS:");
        for (String arg : fn.getArguments()) {
            VmCodeDumper.dumpLine(indent + 2, arg);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_FUNC(" + fn.getName() + "," + fn.getInstructions() + ")";
    }
}
