package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Определение переменной
 */
@SuppressWarnings("UnnecessaryBreak")
@Getter
public class VmInstructionDefine implements VmInstruction {
    // адрес
    private final VmAddress address;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // значение
    private final VmChunk value;

    // конструктор
    public VmInstructionDefine(VmAddress address, String name, boolean hasPrevious, VmChunk value) {
        this.address = address;
        this.name = name;
        this.hasPrevious = hasPrevious;
        this.value = value;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> scope)  {
        if (!hasPrevious) {
            scope.define(address, name, value.runAndGet(vm, scope, address));
        } else {
            Object last = vm.pop(address);
            switch (last) {
                case VmInstance instance -> {
                    instance.getFields().define(address, name, value.runAndGet(vm, scope, address));
                    break;
                }
                case VmUnit unit -> {
                    unit.getFields().define(address, name, value.runAndGet(vm, scope, address));
                    break;
                }
                default -> throw new IllegalStateException("Unexpected value: " + last +
                        " send this error with your code to the developer!");
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE("+name+")");
        VmCodeDumper.dumpLine(indent + 1, "VALUE:");
        for (VmInstruction instruction : value.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "DEFINE_VARIABLE(" + name + ", " + value + ")";
    }
}
