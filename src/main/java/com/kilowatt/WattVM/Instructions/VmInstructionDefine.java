package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Определение переменной
 */
@Getter
public class VmInstructionDefine implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // значение
    private final VmBaseInstructionsBox value;

    // конструктор
    public VmInstructionDefine(VmAddress addr, String name, boolean hasPrevious, VmBaseInstructionsBox value) {
        this.addr = addr;
        this.name = name;
        this.hasPrevious = hasPrevious;
        this.value = value;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        if (!hasPrevious) {
            frame.define(addr, name, value.runAndGet(vm, frame));
        } else {
            Object last = vm.pop();
            switch (last) {
                case VmInstance instance -> {
                    instance.getScope().define(addr, name, value.runAndGet(vm, frame));
                    break;
                }
                case VmUnit unit -> {
                    unit.getFields().define(addr, name, value.runAndGet(vm, frame));
                    break;
                }
                default -> throw new IllegalStateException("Unexpected value: " + last +
                        " send this error with your code to the developer!");
            }
        }
    }

    @Override
    public String toString() {
        return "DEFINE_VARIABLE(" + name + ", " + value + ")";
    }
}
