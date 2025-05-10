package com.kilowatt.Compiler.Builtins.Libraries.Std;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.VmAddress;

/*
 Стд -> Многопоточность
 */
public class StdThreading {
    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "thread interruption error.",
                    "check your code."
            );
        }
    }

    public Thread run(VmFunction fn, WattList args) {
        return WattCompiler.vm.getThreads().submit(() -> {
            WattCompiler.vm.initForThread();

            for (Object o : args.getList()) {
                WattCompiler.vm.push(o);
            }
            fn.exec(WattCompiler.vm, false);
        });
    }
}
