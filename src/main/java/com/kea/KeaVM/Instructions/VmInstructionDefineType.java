package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
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
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        vm.getTypeDefinitions().define(addr, name, type);
    }

    @Override
    public String toString() {
        return "DEFINE_TYPE(" + name + ")";
    }
}
