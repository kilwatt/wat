package com.kilowatt.Compiler.Builtins.Libraries.Std.IO;

import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;

/*
IO -> Функция вывода
 */
public class PrintFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        System.out.print(vm.pop());
    }

    @Override
    public int args() {
        return 1;
    }

    @Override
    public String getName() {
        return "print";
    }
}
