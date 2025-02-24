package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Entities.VmInstance;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Установка значения переменной по имени
 */
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
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        if (!hasPrevious) {
            frame.set(addr, name, value.exec(vm, frame));
        } else {
            Object last = vm.pop();
            switch (last) {
                case VmInstance instance -> {
                    instance.getScope().set(addr, name, value.exec(vm, frame));
                    break;
                }
                case VmUnit unit -> {
                    unit.getFields().set(addr, name, value.exec(vm, frame));
                    break;
                }
                default -> throw new IllegalStateException("Unexpected value: " + last +
                        " send this error with your code to the developer!");
            }
        }
    }

    @Override
    public String toString() {
        return "SET_VARIABLE(" + name + ")";
    }
}
