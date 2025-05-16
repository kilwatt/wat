package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Дублирует значение в стек VM
 */
@Getter
public class VmInstructionDuplicate implements VmInstruction {
    // адресс
    private final VmAddress address;

    // конструктор
    public VmInstructionDuplicate(VmAddress address) {
        this.address = address;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        Object o = vm.pop(address);
        vm.push(o);
        vm.push(o);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DUPLICATE()");
    }

    @Override
    public String toString() {
        return "DUPLICATE_VALUE(" + ")";
    }
}
