package com.kilowatt.Compiler.Builtins.Libraries.Std;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.Errors.WattSemanticError;
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
            VmAddress address = WattCompiler.vm.getLastCallAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "thread interruption error.",
                    "check your code."
            );
        }
    }

    public Thread run(VmFunction fn, WattList list) {
        Thread thread = new Thread(() -> {
            WattCompiler.vm.initForThread();
            for (Object o : list.getArray()) {
                WattCompiler.vm.push(o);
            }
            fn.exec(WattCompiler.vm, false);
        });
        try {
            thread.start();
        } catch (WattParsingError | WattRuntimeError |
                 WattResolveError | WattSemanticError error) {
            error.print();
        } catch (RuntimeException error) {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
            new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "jvm runtime exception: " + error.getMessage(),
                "check your code."
            ).print();
        }
        return thread;
    }
}
