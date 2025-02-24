package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
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
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        Object o = vm.pop();
        vm.push(o);
        vm.push(o);
    }

    @Override
    public String toString() {
        return "DUPLICATE_VALUE(" + ")";
    }
}
