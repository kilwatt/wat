package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

/*
Дублирует значение в стек VM
 */
@Getter
public class VmInstructionDuplicate implements VmInstruction {
    // адресс
    private final VmAddress addr;

    // конструктор
    public VmInstructionDuplicate(VmAddress addr) {
        this.addr = addr;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        Object o = vm.pop(addr);
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
