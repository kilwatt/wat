package com.kea.Compiler;

import com.kea.Compiler.Builtins.KeaErrorBuiltin;
import com.kea.KeaVM.VmAddress;

/*
Провайдер функций
 */
public class KeaBuiltinProvider {
    public final static VmAddress builtinAddress = new VmAddress("builtin", -1);
    public static void provide() {
        KeaCompiler.vm.getGlobals().define(builtinAddress, "error", new KeaErrorBuiltin());
    }
}
