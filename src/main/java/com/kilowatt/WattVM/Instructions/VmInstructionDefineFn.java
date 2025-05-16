package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Определение функции
 */
@Getter
@AllArgsConstructor
public class VmInstructionDefineFn implements VmInstruction {
    // адрес
    private final VmAddress address;
    // функция
    private final VmFunction fn;

    @Override
    public void run(WattVM vm, VmTable<String, Object> table)  {
        // новая копия функции
        VmFunction newFnCopy = fn.copy();
        // по краткому имени
        table.define(address, fn.getName(), newFnCopy);
        // по полному имени
        if (fn.getFullName() != null) table.define(address, fn.getFullName(), newFnCopy);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_FN(" + fn.getName() + ", " + fn.getFullName() + ")");
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
        return "DEFINE_FUNC(" + fn.getName() + ", " + fn.getFullName() + ", " + fn.getBody().getInstructions() + ")";
    }
}
