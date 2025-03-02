package com.kea.Compiler.Builtins;

import java.util.HashMap;

/*
Список библиотек
 */
public class KeaLibraries {
    public static HashMap<String, String> libraries = new HashMap<>() {{
        put("std.io", "std-io.kea");
        put("std.convert", "std-convert.kea");
    }};
}
