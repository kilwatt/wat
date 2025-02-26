package com.kea.Compiler.Builtins;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Builtins.VmBuiltinFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;

/*
Вывод ошибки
 */
public class KeaErrorBuiltin implements VmBuiltinFunction {
    @Override
    public void exec(KeaVM vm, VmAddress address) {
        Object o = vm.pop();
        throw new KeaRuntimeError(address.getLine(), address.getFileName(), o.toString(), "Check your code!");
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
