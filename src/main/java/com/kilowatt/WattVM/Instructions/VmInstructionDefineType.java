package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Определние типа
 */
@Getter
public class VmInstructionDefineType implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя переменной
    private final String name;
    // тип
    private final VmType type;

    // конструктор
    public VmInstructionDefineType(VmAddress addr, String name, VmType type) {
        this.addr = addr;
        this.name = name;
        this.type = type;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        vm.getTypeDefinitions().define(addr, name, type);
    }

    @Override
    public String toString() {
        return "DEFINE_TYPE(" + name + "," + type + ")";
    }
}
