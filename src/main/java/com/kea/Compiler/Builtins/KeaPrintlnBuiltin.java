package com.kea.Compiler.Builtins;

import com.kea.KeaVM.Builtins.VmBuiltinFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;

/*
Вывод
 */
public class KeaPrintlnBuiltin implements VmBuiltinFunction {
    @Override
    public void exec(KeaVM vm, VmAddress address) {
        Object o = vm.pop();
        System.out.println(o.toString());
    }

    @Override
    public int args() {
        return 1;
    }

    @Override
    public String getName() {
        return "println";
    }
}
