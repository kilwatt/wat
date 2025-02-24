package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Удаляет локальную переменную в ВМ
 */
@Getter
public class VmInstructionDelLocal implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // имя переменной
    private final String name;

    public VmInstructionDelLocal(VmAddress addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        frame.getValues().remove(name);
    }

    @Override
    public String toString() {
        return "DELETE_LOCAL(" + name + ")";
    }
}
