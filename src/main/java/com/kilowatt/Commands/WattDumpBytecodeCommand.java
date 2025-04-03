package com.kilowatt.Commands;

import com.kilowatt.Executor.WattExecutor;

import java.io.IOException;

/*
Дамп байткода
 */
public class WattDumpBytecodeCommand implements WattCommand {
    @Override
    public void execute(String... args) throws IOException {
        WattExecutor.dump(args[0]);
    }

    @Override
    public int argsAmount() {
        return 1;
    }
}
