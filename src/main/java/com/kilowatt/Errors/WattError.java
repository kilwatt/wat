package com.kilowatt.Errors;

// интерфейс ошибки
public interface WattError {
    void print();
    int errorCode();
    String message();
    int address();
}
