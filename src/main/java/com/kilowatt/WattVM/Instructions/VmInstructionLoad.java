package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

import java.lang.reflect.Field;

/*
Загрузка переменной по имени в стек
 */
@SuppressWarnings("UnnecessaryBreak")
@Getter
public class VmInstructionLoad implements VmInstruction {
    // адрес
    private final VmAddress address;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // нужно ли пушить результат в стек
    private final boolean shouldPushResult;

    // конструктор
    public VmInstructionLoad(VmAddress address, String name, boolean hasPrevious, boolean shouldPushResult) {
        this.address = address;
        this.name = name;
        this.hasPrevious = hasPrevious;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table)  {
        if (!shouldPushResult) return;
        if (!hasPrevious) {
            if (table.has(name)) {
                vm.push(table.lookup(address, name));
            } else if (vm.getTypeDefinitions().has(name)) {
                vm.push(vm.getTypeDefinitions().lookup(address, name));
            } else if (vm.getUnitDefinitions().has(name)) {
                vm.push(vm.getUnitDefinitions().lookup(address, name));
            } else if (vm.getTraitDefinitions().has(name)) {
                vm.push(vm.getTraitDefinitions().lookup(address, name));
            } else {
                throw new WattRuntimeError(
                    address,
                    "not found: " + name,
                    "did you type wrong name?"
                );
            }
        } else {
            Object last = vm.pop(address);
            switch (last) {
                case VmInstance type -> {
                    vm.push(type.getFields().lookupLocal(address, name));
                    break;
                }
                case VmUnit unit -> {
                    vm.push(unit.getFields().lookupLocal(address, name));
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
                            address,
                            "not found: " + name,
                            "did you type wrong name?"
                        );
                    } catch (IllegalAccessException e) {
                        throw new WattRuntimeError(
                            address,
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
