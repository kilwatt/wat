package com.kilowatt.Compiler.Builtins;

import java.util.HashMap;

/*
Список библиотек
 */
public class WattLibraries {
    public static HashMap<String, String> libraries = new HashMap<>() {{
        put("std.io", "std/std_io.wt");
        put("std.convert", "std/std_convert.wt");
        put("std.math", "std/std_math.wt");
        put("std.random", "std/std_random.wt");
        put("std.system", "std/std_system.wt");
        put("std.jvm", "std/std_jvm.wt");
        put("std.threads", "std/std_threads.wt");
        put("utils.terminal", "utils/utils_terminal.wt");
        put("std.refl", "std/std_reflection.wt");
        put("std.typeof", "std/std_typeof.wt");
        put("utils.wfiglet", "utils/utils_wfiglet.wt");
        put("std.time", "std/std_time.wt");
        put("net.mail", "net/net_mail.wt");
        put("net.owm", "net/net_owm.wt");
        put("net.http", "net/net_http.wt");
        put("std.fs", "std/std_fs.wt");
        put("std.strings", "std/std_strings.wt");
        put("std.collections", "std/std_collections.wt");
        put("utils.log", "utils/utils_log.wt");
        put("utils.tests", "utils/utils_tests.wt");
        put("utils.json", "utils/utils_json.wt");
        put("arc.2d", "arc/arc_2d.wt");
        put("std.serialization", "std/std_serialization.wt");
        put("utils.zlib", "utils/utils_zlib.wt");
        put("utils.stream.api", "utils/utils_stream_api.wt");
        put("utils.crypto", "utils/utils_crypto.wt");
        put("utils.features", "utils/utils_features.wt");
        put("utils.uuid", "utils/utils_uuid.wt");
    }};
}
