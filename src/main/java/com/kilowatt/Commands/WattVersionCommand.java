package com.kilowatt.Commands;

import com.kilowatt.Errors.WattColors;
import com.kilowatt.Watt;

/*
Команды -> Версия
 */
public class WattVersionCommand implements WattCommand {
    @Override
    public void execute(String... args) {
        System.out.println(WattColors.ANSI_YELLOW + "Watt version: " + Watt.version + WattColors.ANSI_RESET);
    }

    @Override
    public int argsAmount() {
        return 0;
    }
}
