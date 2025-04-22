package com.kilowatt.WattVM.Builtins;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;

/*
Билт ин функция
 */
public interface VmBuiltinFunction {
    void exec(WattVM vm, VmAddress address, boolean shouldPushResult);
    int paramsAmount();
    String getName();
}
