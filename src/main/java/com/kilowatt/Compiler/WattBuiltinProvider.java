package com.kilowatt.Compiler;

import com.kilowatt.Compiler.Builtins.Functions.WattAssertBuiltinFn;
import com.kilowatt.Compiler.Builtins.Functions.WattErrorBuiltinFn;
import com.kilowatt.Compiler.Builtins.Functions.WattRangeBuiltinFn;
import com.kilowatt.WattVM.VmAddress;

/*
Провайдер функций
 */
public class WattBuiltinProvider {
    public final static VmAddress builtinAddress = new VmAddress("builtin", -1, -1, "builtin");
    public static void provide() {
        WattCompiler.vm.getGlobals().define(builtinAddress, "error", new WattErrorBuiltinFn());
        WattCompiler.vm.getGlobals().define(builtinAddress, "assert", new WattAssertBuiltinFn());
        WattCompiler.vm.getGlobals().define(builtinAddress, "rng", new WattRangeBuiltinFn());
    }
}
