package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
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
    private final VmAddress addr;
    // юнит
    private final VmUnit unit;
    // тело юнита
    private final VmBaseInstructionsBox body;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // генерация юнита
        unit.getFields().setRoot(frame);
        body.run(vm, unit.getFields());
        unit.getFields().delRoot();
        // бинды функций
        unit.bindFunctionsToUnit();
        // дефайн по имени
        vm.getUnitDefinitions().forceSet(addr, unit.getName(), unit);
        // дефайн по полному имени
        vm.getUnitDefinitions().forceSet(addr, unit.getFullName(), unit);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_UNIT(" + unit.getName() + ", " + unit.getFullName() + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : getBody().getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_UNIT(" + unit.getName() + ", " + unit.getFullName() + ", body: " + body + ")";
    }
}
