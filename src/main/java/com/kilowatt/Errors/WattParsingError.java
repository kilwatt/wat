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
    public void print() {
        System.out.print(WattColors.ANSI_RED);
        System.out.println("╭ 🦜 Parsing error occurred.");
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
}
