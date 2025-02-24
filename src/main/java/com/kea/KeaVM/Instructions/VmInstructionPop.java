package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;

/*
Удаление верхнего значения из стека VM
 */
@SuppressWarnings("unused")
public class VmInstructionPop implements VmInstruction {
    // адресс
    private final VmAddress addr;

    // конструктор
    public VmInstructionPop(VmAddress addr) {
        this.addr = addr;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        vm.pop();
    }

    @Override
    public String toString() {
        return "POP_VALUE()";
    }
}
