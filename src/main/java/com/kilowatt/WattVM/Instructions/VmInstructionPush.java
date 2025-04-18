package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
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
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        vm.push(data);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "PUSH(" + data + ")");
    }

    @Override
    public String toString() {
        return "PUSH_VALUE(" + data + ")";
    }
}
