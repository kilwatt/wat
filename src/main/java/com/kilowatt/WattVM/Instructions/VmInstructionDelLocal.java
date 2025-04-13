package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
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
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        frame.getValues().remove(name);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEL_LOCAL(" + name + ")");
    }

    @Override
    public String toString() {
        return "DELETE_LOCAL(" + name + ")";
    }
}
