package com.kea.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика рантайма
 */
@Getter
@AllArgsConstructor
public class KeaRuntimeError extends RuntimeException implements KeaError {
    private final int line;
    private final String filename;
    private final String message;
    private final String hint;

    @Override
    public void print() {
        System.out.println("╭ 🦜 Runtime error occurred.");
        System.out.println("│ Error?: " + this.message);
        System.out.println("│ Where?: " + filename + "::" + line);
        System.out.println("│ ");
        System.out.println("│ 💡 Hint: " + hint);
        System.out.println("╰");
    }

    @Override
    public int errorCode() {
        return 0;
    }
}
