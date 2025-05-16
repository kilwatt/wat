package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Помещение значения в стек VM
 */
@Getter
public class VmInstructionPush implements VmInstruction {
    // адрес
    private final VmAddress address;
    // данные для пуша
    private final Object data;

    // конструктор
    public VmInstructionPush(VmAddress address, Object data) {
        this.address = address;
        this.data = data;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
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
