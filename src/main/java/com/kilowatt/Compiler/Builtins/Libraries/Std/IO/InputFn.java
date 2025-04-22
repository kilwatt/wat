package com.kilowatt.Compiler.Builtins.Libraries.Std.IO;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
IO -> Функция Инпута
 */
public class InputFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // ридер
        BufferedReader r = new BufferedReader(
                new InputStreamReader(System.in));
        // читаем и пушим, если надо
        try {
            Object value = r.readLine();
            if (!shouldPushResult) return;
            vm.push(value);
        } catch (IOException e) {
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io error with input fn: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    @Override
    public int paramsAmount() {
        return 0;
    }

    @Override
    public String getName() {
        return "input";
    }
}
