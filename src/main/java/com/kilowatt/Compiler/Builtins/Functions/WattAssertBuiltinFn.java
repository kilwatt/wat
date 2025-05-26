package com.kilowatt.Compiler.Builtins.Functions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;

/*
Функция ассерт
 */
public class WattAssertBuiltinFn extends VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // получаем объект из стека
        Object b = vm.pop(address);
        // проверяем
        if (b instanceof Boolean bool) {
            if (!bool) {
                throw new WattRuntimeError(
                    address,
                    "assertion error",
                    "check your code"
                );
            }
        } else {
            throw new WattRuntimeError(
                address,
                b + " not a bool",
                "check your code."
            );
        }
        // проверка на пуш
        if (shouldPushResult) vm.push(null);
    }

    @Override
    public int paramsAmount() {
        return 1;
    }

    @Override
    public String getName() {
        return "error";
    }
}
