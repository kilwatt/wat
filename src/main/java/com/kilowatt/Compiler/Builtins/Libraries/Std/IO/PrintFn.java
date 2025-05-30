package com.kilowatt.Compiler.Builtins.Libraries.Std.IO;

import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;

/*
IO -> Функция вывода
 */
public class PrintFn extends VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // вывод
        System.out.print(vm.pop(address));
        // проверка на пуш
        if (shouldPushResult) vm.push(null);
    }

    @Override
    public int paramsAmount() {
        return 1;
    }

    @Override
    public String getName() {
        return "print";
    }
}
