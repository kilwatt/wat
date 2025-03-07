package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
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
        Object o = vm.pop();
        vm.push(o);
        vm.push(o);
    }

    @Override
    public String toString() {
        return "DUPLICATE_VALUE(" + ")";
    }
}
