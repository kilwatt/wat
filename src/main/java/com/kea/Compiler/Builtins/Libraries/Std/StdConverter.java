package com.kea.Compiler.Builtins.Libraries.Std;

import com.kea.KeaVM.VmAddress;

/*
Стд -> Конвертация типов
 */
public class StdConverter {
    public int to_int(VmAddress address, Number i) {
        return i.intValue();
    }
    public float to_float(VmAddress address, Number i) {
        return i.floatValue();
    }
    public long to_long(VmAddress address, Number i) {
        return i.longValue();
    }
}
