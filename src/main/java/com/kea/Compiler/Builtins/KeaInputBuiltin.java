package com.kea.Compiler.Builtins;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Builtins.VmBuiltinFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
Вывод
 */
public class KeaInputBuiltin implements VmBuiltinFunction {
    @Override
    public void exec(KeaVM vm, VmAddress address) {
        Object o = vm.pop();
        System.out.println(o.toString());
        BufferedReader r = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            vm.push(r.readLine());
        } catch (IOException e) {
            throw new KeaRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "Some IO error occured with input function: " + e.getMessage(),
                    "Check your code!"
            );
        }
    }

    @Override
    public int args() {
        return 1;
    }

    @Override
    public String getName() {
        return "input";
    }
}
