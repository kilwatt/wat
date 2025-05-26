package com.kilowatt.Compiler.Builtins.Libraries.Std.IO;

import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

/*
IO -> Функция вывода строки
 */
public class PrintlnFn extends VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // получаем объект
        Object value = vm.pop(address);
        // выводим
        System.out.println(value);
        // проверка на пуш
        if (shouldPushResult) vm.push(null);
    }

    @Override
    public int paramsAmount() {
        return 1;
    }

    @Override
    public String getName() {
        return "println";
    }
}
