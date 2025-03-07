package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;

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
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        vm.pop();
    }

    @Override
    public String toString() {
        return "POP_VALUE()";
    }
}
