package com.kea.KeaVM.Builtins;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;

/*
Билт ин функция
 */
public interface VmBuiltinFunction {
    void exec(KeaVM vm, VmAddress address);
    int args();
    String getName();
}
