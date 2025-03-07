package com.kilowatt.Compiler.Builtins.Libraries.Std.IO;

import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

/*
IO -> Функция Вывода
 */
public class PrintlnFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address) {
        System.out.println(vm.pop());
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
