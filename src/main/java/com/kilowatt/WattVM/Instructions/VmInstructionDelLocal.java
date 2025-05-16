package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Удаляет локальную переменную в ВМ
 */
@Getter
public class VmInstructionDelLocal implements VmInstruction {
    // адресс
    private final VmAddress address;
    // имя переменной
    private final String name;

    public VmInstructionDelLocal(VmAddress address, String name) {
        this.address = address;
        this.name = name;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        table.getValues().remove(name);
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
