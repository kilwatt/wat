package com.kilowatt;
import com.kilowatt.Commands.WattCommandsExecutor;

import java.io.IOException;

/*
Язык программирования WATT
 */
public class Watt {
    // версия
    public static final String version = "commit.264";

    // экзекьютор команд
    public static final WattCommandsExecutor commandExecutor = new WattCommandsExecutor();

    // мэйн функция
    public static void main(String[] args) throws IOException {
        commandExecutor.exec(args);
    }
}