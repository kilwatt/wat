package com.kilowatt.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика по jvm
 */
@Getter
@AllArgsConstructor
public class WattInternalError extends RuntimeException implements WattError {
    private final int line;
    private final String filename;
    private final String message;
    private final String hint;
    private final StackTraceElement[] trace;

    @Override
    public void panic() {
        System.out.print(WattColors.ANSI_RED);
        System.out.println("╭ ⚡ internal error.");
        System.out.println("│ err: " + this.message);
        System.out.println("│ at: " + filename + "::" + line);
        System.out.println("│ 💡 " + hint);
        System.out.println("│———————————————");
        System.out.println("│ calls trace: ");
        for (StackTraceElement e : trace) {
            System.out.println("│ " + e);
        }
        System.out.println("╰");
        System.out.print(WattColors.ANSI_RESET);
        System.exit(errorCode());
    }

    @Override
    public int errorCode() {
        return 2;
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
