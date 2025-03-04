package com.kea.Compiler.Builtins.Libraries.Std.IO;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Builtins.VmBuiltinFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
IO -> Функция Инпута
 */
public class InputFn implements VmBuiltinFunction {
    @Override
    public void exec(KeaVM vm, VmAddress address) {
        BufferedReader r = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            vm.push(r.readLine());
        } catch (IOException e) {
            throw new KeaRuntimeError(
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
