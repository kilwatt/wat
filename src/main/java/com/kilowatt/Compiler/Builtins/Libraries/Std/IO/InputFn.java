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
    public void exec(WattVM vm, VmAddress address) {
        BufferedReader r = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            vm.push(r.readLine());
        } catch (IOException e) {
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "Some IO error occurred with input function: " + e.getMessage(),
                    "Check your code!"
            );
        }
    }

    @Override
    public int args() {
        return 0;
    }

    @Override
    public String getName() {
        return "input";
    }
}
