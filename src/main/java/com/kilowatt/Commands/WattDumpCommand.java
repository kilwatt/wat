package com.kilowatt.Commands;

import com.kilowatt.Executor.WattExecutor;

import java.io.IOException;

/*
Дамп байткода
 */
public class WattDumpCommand implements WattCommand {
    @Override
    public void execute(String... args) throws IOException {
        if (args.length != 1) {
            throw new WattCommandError("Invalid usage.");
        }
        WattExecutor.dump(args[0], false);
    }
}
