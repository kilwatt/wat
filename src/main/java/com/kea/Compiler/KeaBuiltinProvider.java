package com.kea.Compiler;

import com.kea.Compiler.Builtins.KeaPrintlnBuiltin;
import com.kea.KeaVM.VmAddress;

/*
Провайдер функций
 */
public class KeaBuiltinProvider {
    private final static VmAddress builtinAddress = new VmAddress("builtin", -1);
    public static void provide() {
        KeaCompiler.vm.getGlobals().define(builtinAddress, "println", new KeaPrintlnBuiltin());
    }
}
