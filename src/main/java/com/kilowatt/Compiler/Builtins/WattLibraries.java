package com.kilowatt.Compiler.Builtins;

import java.util.HashMap;

/*
Список библиотек
 */
public class WattLibraries {
    public static HashMap<String, String> libraries = new HashMap<>() {{
        put("std.io", "std/std-io.w");
        put("std.convert", "std/std-convert.w");
        put("std.math", "std/std-math.w");
        put("std.math.ext", "std/std-math-ext.w");
    }};
}
