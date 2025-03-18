package com.kilowatt.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика ресолва файла
 */
@Getter
@AllArgsConstructor
public class WattResolveError extends RuntimeException implements WattError {
    private final int line;
    private final String filename;
    private final String message;
    private final String hint;

    @Override
    public void print() {
        System.out.print(WattColors.ANSI_RED);
        System.out.println("╭ 🦜 Resolving error occurred.");
        System.out.println("│ Error?: " + this.message);
        System.out.println("│ Where?: " + filename + "::" + line);
        System.out.println("│ ");
        System.out.println("│ 💡 Hint: " + hint);
        System.out.println("╰");
        System.out.print(WattColors.ANSI_RESET);
    }

    @Override
    public int errorCode() {
        return 0;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int address() {
        return line;
    }
}
