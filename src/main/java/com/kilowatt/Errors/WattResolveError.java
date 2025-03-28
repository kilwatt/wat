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
        System.out.println("╭ ⚡ resolving error.");
        System.out.println("│ err: " + this.message);
        System.out.println("│ at: " + filename + ":" + line);
        System.out.println("╰ 💡 " + hint);
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
