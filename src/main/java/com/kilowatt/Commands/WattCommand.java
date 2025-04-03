package com.kilowatt.Commands;

import java.io.IOException;

/*
Комманда для Ватта
 */
public interface WattCommand {
    void execute(String... args) throws IOException;
    int argsAmount();
}
