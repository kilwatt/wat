package com.kea.Compiler.Builtins.Libraries.Std.IO;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Builtins.VmBuiltinFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
IO -> Функция Вывода
 */
public class PrintlnFn implements VmBuiltinFunction {
    @Override
    public void exec(KeaVM vm, VmAddress address) {
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
