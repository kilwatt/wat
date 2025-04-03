package com.kilowatt.Commands;

import com.kilowatt.Executor.WattExecutor;

import java.io.IOException;

/*
Дамп байткода в файл
 */
public class WattDumpFileCommand implements WattCommand {
    @Override
    public void execute(String... args) throws IOException {
        WattExecutor.dump(args[0], true);
    }

    @Override
    public int argsAmount() {
        return 1;
    }
}
