package com.kilowatt.Compiler.Builtins.Libraries.Std.System;

import lombok.AllArgsConstructor;
import lombok.Getter;

// процесс
@AllArgsConstructor
@Getter
public class SystemProcess {
    private final Process process;
    public long pid() { return process.pid(); }
    public boolean isAlive() { return process.isAlive(); }
    public void kill() { process.destroy(); }
}
