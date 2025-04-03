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
}
