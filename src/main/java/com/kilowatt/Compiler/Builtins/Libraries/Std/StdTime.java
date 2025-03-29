package com.kilowatt.Compiler.Builtins.Libraries.Std;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
Библиотека времени
 */
public class StdTime {
    // форматирование времени
    public String format(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");
        return time.format(formatter);
    }

    // получение текущего времени
    public LocalTime now() {
        return LocalTime.now();
    }
}
