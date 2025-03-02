package com.kea.Compiler.Builtins.Libraries.Std;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.VmAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
Библиотека IO
 */
public class StdIO {
    public void println(VmAddress address, Object o) {
        System.out.println(o);
    }

    public String input(VmAddress address) {
        BufferedReader r = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            return r.readLine();
        } catch (IOException e) {
            throw new KeaRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "Some IO error occurred with input function: " + e.getMessage(),
                    "Check your code!"
            );
        }
    }
}
