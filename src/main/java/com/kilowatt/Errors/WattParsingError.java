package com.kilowatt.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика парсинга
 */
@Getter
@AllArgsConstructor
public class WattParsingError extends RuntimeException implements WattError {
    private final int line;
    private final String filename;
    private final String message;
    private final String hint;

    @Override
    public void panic() {
        System.out.print(WattColors.ANSI_RED);
        System.out.println("╭ ⚡ parsing error.");
        System.out.println("│ err: " + this.message);
        System.out.println("│ at: " + filename + ":" + line);
        System.out.println("╰ 💡 " + hint);
        System.out.print(WattColors.ANSI_RESET);
        System.exit(errorCode());
    }

    @Override
    public int errorCode() {
        return 1;
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
