package com.kea.KeaVM.Instructions;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

/*
Определение юнита
 */
@Getter
public class VmInstructionDefineUnit implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя переменной
    private final String name;
    // тело юнита
    private final VmBaseInstructionsBox body;

    // конструктор
    public VmInstructionDefineUnit(VmAddress addr, String name, VmBaseInstructionsBox body) {
        this.addr = addr;
        this.name = name;
        this.body = body;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        if (vm.getUnitDefinitions().has(name)) {
            throw new KeaRuntimeError(
                    addr.getLine(), addr.getFileName(),
                    "Already defined unit with name: " + name, "Check your code!");
        }
        VmUnit unit = new VmUnit(name, new VmFrame<>());
        unit.getFields().setRoot(frame);
        body.execWithoutPop(vm, unit.getFields());
        unit.getFields().delRoot();
        unit.getFields().setRoot(vm.getGlobals());
        vm.getUnitDefinitions().define(addr, name, unit);
    }

    @Override
    public String toString() {
        return "DEFINE_UNIT(" + name + ")";
    }
}
