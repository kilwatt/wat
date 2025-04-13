package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
Определние типа
 */
@Getter
@RequiredArgsConstructor
public class VmInstructionDefineType implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // тип
    private final VmType type;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // дефайн по имени
        vm.getTypeDefinitions().forceSet(addr, type.getName(), type);
        // дефайн по полному имени
        if (type.getName() != null) vm.getTypeDefinitions().forceSet(addr, type.getFullName(), type);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_TYPE(" + type.getName() + ", " + type.getFullName() + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : type.getBody().getInstructionContainer()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent + 1, "ARGS:");
        for (String arg : type.getConstructor()) {
            VmCodeDumper.dumpLine(indent + 2, arg);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_TYPE(" + type.getName() + ", " + type.getFullName() + ", " + type + ")";
    }
}
