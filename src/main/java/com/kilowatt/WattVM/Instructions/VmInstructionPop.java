package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Удаление верхнего значения из стека VM
 */
@SuppressWarnings("unused")
@Getter
public class VmInstructionPop implements VmInstruction {
    // адресс
    private final VmAddress address;

    // конструктор
    public VmInstructionPop(VmAddress address) {
        this.address = address;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        vm.pop(address);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "POP()");
    }

    @Override
    public String toString() {
        return "POP_VALUE()";
    }
}
