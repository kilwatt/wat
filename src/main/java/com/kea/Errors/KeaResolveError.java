package com.kea.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика ресолва файла
 */
@Getter
@AllArgsConstructor
public class KeaResolveError extends RuntimeException implements KeaError {
    private final int line;
    private final String filename;
    private final String message;
    private final String hint;

    @Override
    public void print() {
        System.out.print(KeaColors.ANSI_RED);
        System.out.println("╭ 🦜 Resolving error occurred.");
        System.out.println("│ Error?: " + this.message);
        System.out.println("│ Where?: " + filename + "::" + line);
        System.out.println("│ ");
        System.out.println("│ 💡 Hint: " + hint);
        System.out.println("╰");
        System.out.print(KeaColors.ANSI_RESET);
    }

    @Override
    public int errorCode() {
        return 0;
    }
}
