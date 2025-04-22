package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Определение функции
 */
@Getter
@AllArgsConstructor
public class VmInstructionDefineFn implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя функции
    private final String name;
    // полное имя
    private final String fullName;
    // функция
    private final VmFunction fn;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // новая копия функции
        VmFunction newFnCopy = fn.copy();
        // по краткому имени
        frame.define(addr, name,newFnCopy);
        // по полному имени
        if (fullName != null) {
            frame.define(addr, fullName, newFnCopy);
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_FN(" + fn.getName() + ", " + fullName + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : fn.getBody().getInstructions()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent + 1, "ARGS:");
        for (String arg : fn.getParams()) {
            VmCodeDumper.dumpLine(indent + 2, arg);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_FUNC(" + fn.getName() + ", " + fullName + ", " + fn.getBody().getInstructions() + ")";
    }
}
