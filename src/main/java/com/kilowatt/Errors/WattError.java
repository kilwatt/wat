package com.kilowatt.Errors;

// интерфейс ошибки
public interface WattError {
    void panic();
    int errorCode();
    String message();
    int address();
}
