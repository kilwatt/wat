package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
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
    // тело юнита
    private final VmBaseInstructionsBox body;

    // конструктор
    public VmInstructionDefineUnit(VmAddress addr, String name, VmBaseInstructionsBox body) {
        this.addr = addr;
        this.name = name;
        this.body = body;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        if (vm.getUnitDefinitions().has(name)) {
            throw new WattRuntimeError(
                    addr.getLine(), addr.getFileName(),
                    "unit already defined: " + name, "did you forget to change name?");
        }
        VmUnit unit = new VmUnit(name, new VmFrame<>());
        unit.getFields().setRoot(frame);
        body.run(vm, unit.getFields());
        unit.getFields().delRoot();
        unit.getFields().setRoot(vm.getGlobals());
        vm.getUnitDefinitions().define(addr, name, unit);
    }

    @Override
    public String toString() {
        return "DEFINE_UNIT(" + name + ", body: " + body + ")";
    }
}
