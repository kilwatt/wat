package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Определение юнита
 */
@Getter
public class VmInstructionDefineUnit implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя переменной
    private final String name;
    // юнит
    private final VmUnit unit;

    // конструктор
    public VmInstructionDefineUnit(VmAddress addr, String name, VmUnit unit) {
        this.addr = addr;
        this.name = name;
        this.unit = unit;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        vm.getUnitDefinitions().define(addr, name, unit);
    }

    @Override
    public String toString() {
        return "DEFINE_UNIT(" + name + ")";
    }
}
