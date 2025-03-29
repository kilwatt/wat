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
        put("std.random", "std/std-random.w");
        put("std.sys", "std/std-sys.w");
        put("std.jvm", "std/std-jvm.w");
        put("std.threads", "std/std-threads.w");
        put("ext.tui", "ext/ext-tui.w");
        put("std.refl", "std/std-reflection.w");
        put("ext.wfiglet", "ext/ext-wfiglet.w");
        put("std.time", "std/std-time.w");
    }};
}
