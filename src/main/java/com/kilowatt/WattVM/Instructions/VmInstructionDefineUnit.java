package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Определение юнита
 */
@Getter
@AllArgsConstructor
public class VmInstructionDefineUnit implements VmInstruction {
    // адрес
    private final VmAddress address;
    // юнит
    private final VmUnit unit;
    // тело юнита
    private final VmChunk body;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // генерация юнита
        unit.getFields().setRoot(vm.getGlobals());
        // временные self, для выполнения тела
        unit.getFields().define(address, "self", this);
        // выполнения тела
        body.run(vm, unit.getFields());
        // удаление временного self
        unit.getFields().getValues().remove("self");
        // бинды функций
        unit.bindFunctionsToUnit();
        // дефайн по имени
        vm.getUnitDefinitions().forceSet(address, unit.getName(), unit);
        // дефайн по полному имени
        if (unit.getFullName() != null) vm.getUnitDefinitions().forceSet(address, unit.getFullName(), unit);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_UNIT(" + unit.getName() + ", " + unit.getFullName() + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : getBody().getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_UNIT(" + unit.getName() + ", " + unit.getFullName() + ", body: " + body + ")";
    }
}
