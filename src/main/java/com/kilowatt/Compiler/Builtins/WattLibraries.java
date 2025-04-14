package com.kilowatt.Compiler.Builtins;

import java.util.HashMap;

/*
Список библиотек
 */
public class WattLibraries {
    public static HashMap<String, String> libraries = new HashMap<>() {{
        put("std.io", "std/std-io.wt");
        put("std.convert", "std/std-convert.wt");
        put("std.math", "std/std-math.wt");
        put("std.math.ext", "std/std-math-ext.wt");
        put("std.random", "std/std-random.wt");
        put("std.sys", "std/std-sys.wt");
        put("std.jvm", "std/std-jvm.wt");
        put("std.threads", "std/std-threads.wt");
        put("ext.tui", "ext/ext-tui.wt");
        put("std.refl", "std/std-reflection.wt");
        put("std.typeof", "std/std-typeof.wt");
        put("ext.wfiglet", "ext/ext-wfiglet.wt");
        put("std.time", "std/std-time.wt");
        put("net.mail", "net/net-mail.wt");
        put("net.owm", "net/net-owm.wt");
        put("std.fs", "std/std-fs.wt");
        put("std.strings", "std/std-strings.wt");
        put("std.collections", "std/std-collections.wt");
        put("utils.log", "utils/utils-log.wt");
        put("utils.tests", "utils/utils-tests.wt");
        put("utils.json", "utils/utils-json.wt");
        put("arc.2d", "arc/arc-2d.wt");
    }};
}
