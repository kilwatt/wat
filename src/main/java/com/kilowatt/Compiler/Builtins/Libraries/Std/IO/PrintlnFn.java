package com.kilowatt.Compiler.Builtins.Libraries.Std.IO;

import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

/*
IO -> Функция вывода строки
 */
public class PrintlnFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // получаем объект
        Object value = vm.pop();
        // нужно ли пушить
        if (!shouldPushResult) return;
        // выводим
        System.out.println(value);
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
