package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
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
    // имя юнита
    private final String name;
    // полное имя юнита
    private final String fullName;
    // тело юнита
    private final VmBaseInstructionsBox body;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        if (vm.getUnitDefinitions().has(name)) {
            throw new WattRuntimeError(
                    addr.getLine(), addr.getFileName(),
                    "unit already defined: " + name, "did you forget to change name?");
        }
        // генерация юнита
        VmUnit unit = new VmUnit(name, new VmFrame<>());
        unit.getFields().setRoot(frame);
        body.run(vm, unit.getFields());
        unit.getFields().delRoot();
        // дефайн по имени
        vm.getUnitDefinitions().forceSet(addr, name, unit);
        // дефайн по полному имени
        if (fullName != null) {
            vm.getUnitDefinitions().forceSet(addr, fullName, unit);
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_UNIT(" + name + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : getBody().getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_UNIT(" + name + ", body: " + body + ")";
    }
}
