package com.kilowatt.Compiler.Builtins.Libraries.Std;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
Стд -> Время
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

    // получение текущей даты
    public LocalDate date() {
        return LocalDate.now();
    }
}
