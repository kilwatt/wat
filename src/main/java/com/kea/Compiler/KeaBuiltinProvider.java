package com.kea.Compiler;

import com.kea.Compiler.Builtins.KeaErrorBuiltin;
import com.kea.Compiler.Builtins.KeaInputBuiltin;
import com.kea.Compiler.Builtins.KeaPrintlnBuiltin;
import com.kea.KeaVM.VmAddress;

/*
Провайдер функций
 */
public class KeaBuiltinProvider {
    private final static VmAddress builtinAddress = new VmAddress("builtin", -1);
    public static void provide() {
        KeaCompiler.vm.getGlobals().define(builtinAddress, "println", new KeaPrintlnBuiltin());
        KeaCompiler.vm.getGlobals().define(builtinAddress, "error", new KeaErrorBuiltin());
        KeaCompiler.vm.getGlobals().define(builtinAddress, "input", new KeaInputBuiltin());
    }
}
