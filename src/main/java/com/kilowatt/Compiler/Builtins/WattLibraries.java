package com.kilowatt.Compiler.Builtins;

import java.util.HashMap;

/*
Список библиотек
 */
public class WattLibraries {
    public static HashMap<String, String> libraries = new HashMap<>() {{
        put("std.io", "std-io.w");
        put("std.convert", "std-convert.w");
    }};
}
