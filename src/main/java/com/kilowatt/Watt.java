package com.kilowatt;
import com.kilowatt.Commands.WattCommandsHandler;

import java.io.IOException;

/*
Язык программирования WATT
 */
public class Watt {
    // версия
    public static final String version = "0.0.1";

    // хэндлер команд
    public static final WattCommandsHandler commandHandler = new WattCommandsHandler();

    // мэйн функция
    public static void main(String[] args) throws IOException {
        commandHandler.handle(args);
    }
}