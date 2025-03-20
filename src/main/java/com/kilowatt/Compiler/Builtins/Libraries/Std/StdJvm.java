package com.kilowatt.Compiler.Builtins.Libraries.Std;

import java.lang.reflect.Array;

/*
 Стд -> JVM
 */
public class StdJvm {
    public int array_length(Object o) {
        return Array.getLength(o);
    }

    public Object array_element(Object o, int index) {
        return Array.get(o, index);
    }
}
