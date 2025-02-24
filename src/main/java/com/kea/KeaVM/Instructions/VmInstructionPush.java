package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Помещение значения в стек VM
 */
@Getter
public class VmInstructionPush implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // данные для пуша
    private final Object data;

    // конструктор
    public VmInstructionPush(VmAddress addr, Object data) {
        this.addr = addr;
        this.data = data;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        vm.push(data);
    }

    @Override
    public String toString() {
        return "PUSH_VALUE(" + data + ")";
    }
}
