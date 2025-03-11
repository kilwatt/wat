package com.kilowatt.Compiler.Builtins.Libraries.Std;

import com.kilowatt.WattVM.VmAddress;

/*
Стд -> Конвертация типов
 */
public class StdConverter {
    public int to_int(Number i) {
        return i.intValue();
    }
    public float to_float(Number i) {
        return i.floatValue();
    }
    public long to_long(Number i) {
        return i.longValue();
    }
    public String to_string(Object o) {
        return o.toString();
    }
}
