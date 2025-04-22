package com.kilowatt.Compiler.Builtins.Libraries.Std.TypeOf;

import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;

/*
TypeOf -> Функция получения полного имени типа объекта
 */
public class FTypeOf implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // получаем объект
        Object o = vm.pop(address);
        // проверяем, нужно ли пушить
        if (!shouldPushResult) return;
        // если тип или юнит
        switch (o) {
            case VmType type -> vm.push(type.getFullName());
            case VmUnit unit -> vm.push(unit.getFullName());
            case VmInstance instance -> vm.push(instance.getType().getFullName());
            case Integer i -> vm.push("int");
            case Boolean b -> vm.push("bool");
            case String string -> vm.push("string");
            case Float v -> vm.push("float");
            case null -> vm.push("null");
            default -> vm.push(o.getClass().getName());
        }
    }

    @Override
    public int paramsAmount() {
        return 1;
    }

    @Override
    public String getName() {
        return "typeof";
    }
}
