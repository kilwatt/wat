package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

import java.lang.reflect.Field;

/*
Загрузка переменной по имени в стек
 */
@SuppressWarnings("UnnecessaryBreak")
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
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        if (!shouldPushResult) return;
        if (!hasPrevious) {
            if (frame.has(name)) {
                vm.push(frame.lookup(addr, name));
            } else if (vm.getTypeDefinitions().has(name)) {
                vm.push(vm.getTypeDefinitions().lookup(addr, name));
            } else if (vm.getUnitDefinitions().has(name)) {
                vm.push(vm.getUnitDefinitions().lookup(addr, name));
            } else {
                throw new WattRuntimeError(
                        addr.getLine(), addr.getFileName(),
                        "not found: " + name,
                        "did you type wrong name?"
                );
            }
        } else {
            Object last = vm.pop(addr);
            switch (last) {
                case VmInstance type -> {
                    vm.push(type.getFields().lookup(addr, name));
                    break;
                }
                case VmUnit unit -> {
                    vm.push(unit.getFields().lookup(addr, name));
                    break;
                }
                case null -> throw new IllegalStateException(
                        "unexpected value: null. send this error with your code to the developer!"
                );
                default -> {
                    Class<?> clazz = last.getClass();
                    try {
                        Field field = clazz.getField(name);
                        field.setAccessible(true);
                        vm.push(field.get(last));
                    } catch (NoSuchFieldException e) {
                        throw new WattRuntimeError(
                                addr.getLine(),
                                addr.getFileName(),
                                "field not found: " + name,
                                "check your reflection interaction."
                        );
                    } catch (IllegalAccessException e) {
                        throw new WattRuntimeError(
                                addr.getLine(),
                                addr.getFileName(),
                                "access failed: " + name,
                                "check your reflection interaction."
                        );
                    }
                }
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "LOAD("+name+")");
    }

    @Override
    public String toString() {
        return "LOAD_VARIABLE(" + name + ",push:" + shouldPushResult + ")";
    }
}
