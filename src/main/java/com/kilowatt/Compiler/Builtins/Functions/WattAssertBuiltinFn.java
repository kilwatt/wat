package com.kilowatt.Compiler.Builtins.Functions;

import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;

/*
Функция ассерт
 */
public class WattAssertBuiltinFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        Object b = vm.pop();
        if (b instanceof Boolean bool) {
            if (!bool) {
                throw new WattParsingError(
                    address.getLine(),
                    address.getFileName(),
                    "assertion error",
                    "check your code"
                );
            }
        } else {
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                b + " not a bool",
                "check your code."
            );
        }
    }

    @Override
    public int args() {
        return 1;
    }

    @Override
    public String getName() {
        return "error";
    }
}
