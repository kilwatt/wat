package com.kilowatt.Errors;

// интерфейс ошибки
public abstract class WattError extends RuntimeException {
    public abstract void panic();
    public abstract int errorCode();
    public abstract String message();
    public abstract int address();
}
