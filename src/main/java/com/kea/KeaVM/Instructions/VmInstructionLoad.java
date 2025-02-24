package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Загрузка переменной по имени в стек
 */
@Getter
public class VmInstructionLoad implements VmInstruction {
    // адрес
    private final VmAddress addr;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // нужно ли пушить результат в стек
    private final boolean shouldPushResult;

    // конструктор
    public VmInstructionLoad(VmAddress addr, String name, boolean hasPrevious, boolean shouldPushResult) {
        this.addr = addr;
        this.name = name;
        this.hasPrevious = hasPrevious;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        if (!shouldPushResult) return;
        if (!hasPrevious) {
            if (frame.has(name)) {
                vm.push(frame.lookup(addr, name));
            } else if (vm.getTypeDefinitions().has(name)) {
                vm.push(vm.getTypeDefinitions().lookup(addr, name));
            } else if (vm.getTypeDefinitions().has(name)) {
                vm.push(vm.getTypeDefinitions().lookup(addr, name));
            }
        } else {
            Object last = vm.pop();
            switch (last) {
                case VmType type -> {
                    vm.push(type.getFields().lookup(addr, name));
                    break;
                }
                case VmUnit unit -> {
                    vm.push(unit.getFields().lookup(addr, name));
                    break;
                }
                default -> throw new IllegalStateException("Unexpected value: " + last +
                        " send this error with your code to the developer!");
            }
        }
    }

    @Override
    public String toString() {
        return "LOAD_VARIABLE(" + name + ")";
    }
}
