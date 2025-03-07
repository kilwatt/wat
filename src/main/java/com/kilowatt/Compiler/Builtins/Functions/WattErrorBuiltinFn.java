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
        Object o = vm.pop();
        throw new WattRuntimeError(address.getLine(), address.getFileName(), o.toString(), "Check your code!");
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
