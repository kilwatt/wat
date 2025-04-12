package com.kilowatt.Commands;

import com.kilowatt.Executor.WattExecutor;

import java.io.IOException;

/*
Дамп байткода в файл
 */
public class WattDumpFileCommand implements WattCommand {
    @Override
    public void execute(String... args) throws IOException {
        if (args.length != 1) {
            throw new WattCommandError("Invalid usage.");
        }
        WattExecutor.dump(args[0], true);
    }
}
