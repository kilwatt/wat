package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

/*
Установка значения переменной по имени
 */
@SuppressWarnings("UnnecessaryBreak")
@Getter
public class VmInstructionSet implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // значение
    private final VmBaseInstructionsBox value;

    // конструктор
    public VmInstructionSet(VmAddress addr, String name, boolean hasPrevious, VmBaseInstructionsBox value) {
        this.addr = addr;
        this.name = name;
        this.hasPrevious = hasPrevious;
        this.value = value;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        if (!hasPrevious) {
            frame.set(addr, name, value.runAndGet(vm, addr, frame));
        } else {
            Object last = vm.pop(addr);
            switch (last) {
                case VmInstance instance -> {
                    instance.getFields().set(addr, name, value.runAndGet(vm, addr, frame));
                    break;
                }
                case VmUnit unit -> {
                    unit.getFields().set(addr, name, value.runAndGet(vm, addr, frame));
                    break;
                }
                default -> throw new IllegalStateException("Unexpected value: " + last +
                        " send this error with your code to the developer!");
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "SET("+name+")");
        VmCodeDumper.dumpLine(indent + 1, "VALUE:");
        for (VmInstruction instruction : value.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "SET_VARIABLE(" + name + ", " + value + ")";
    }
}
