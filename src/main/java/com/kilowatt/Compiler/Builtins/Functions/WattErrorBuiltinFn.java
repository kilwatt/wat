package com.kilowatt.Compiler.Builtins.Functions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

/*
Функция создание рантайм ошибки
 */
public class WattErrorBuiltinFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // получаем инфу об ошибке
        Object hint = vm.pop(address);
        Object text = vm.pop(address);
        // создаём ошибку
        WattRuntimeError error = new WattRuntimeError(
            address.getLine(),
            address.getFileName(),
            text.toString(),
            hint.toString()
        );
        // пушим
        if (shouldPushResult) vm.push(error);
    }

    @Override
    public int args() {
        return 2;
    }

    @Override
    public String getName() {
        return "error";
    }
}
