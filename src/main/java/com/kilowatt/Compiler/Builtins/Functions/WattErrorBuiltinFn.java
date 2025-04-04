package com.kilowatt.Compiler.Builtins.Functions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

/*
Вывод ошибки
 */
public class WattErrorBuiltinFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address) {
        Object hint = vm.pop();
        Object text = vm.pop();
        vm.push(new WattRuntimeError(address.getLine(), address.getFileName(), text.toString(), hint.toString()));
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
