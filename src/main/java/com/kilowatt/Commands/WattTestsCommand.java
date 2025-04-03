package com.kilowatt.Commands;

import com.kilowatt.Testrunner.WattTests;

/*
Команды -> Тесты
 */
public class WattTestsCommand implements WattCommand {
    @Override
    public void execute(String... args) {
        WattTests.run();
    }

    @Override
    public int argsAmount() {
        return 0;
    }
}
