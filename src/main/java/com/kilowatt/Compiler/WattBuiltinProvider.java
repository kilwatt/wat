package com.kilowatt.Compiler;

import com.kilowatt.Compiler.Builtins.Functions.WattErrorBuiltinFn;
import com.kilowatt.WattVM.VmAddress;

/*
Провайдер функций
 */
public class WattBuiltinProvider {
    public final static VmAddress builtinAddress = new VmAddress("builtin", -1);
    public static void provide() {
        WattCompiler.vm.getGlobals().define(builtinAddress, "error", new WattErrorBuiltinFn());
    }
}
