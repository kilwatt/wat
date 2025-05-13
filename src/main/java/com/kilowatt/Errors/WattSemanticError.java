package com.kilowatt.Errors;

import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика по семантике
 */
@Getter
@AllArgsConstructor
public class WattSemanticError extends WattError {
    private final VmAddress address;
    private final String message;
    private final String hint;

    @Override
    public void panic() {
        System.out.print(WattColors.ANSI_RED);
        System.out.println(WattColors.ANSI_RED + "error: " + WattColors.ANSI_RESET + message);
        System.out.println();
        System.out.println("┌─ " + address.getFileName() + ":" + address.getLine() + ":" + address.getColumn());
        System.out.println("│ " + address.getLineText());
        System.out.println("│ " + " ".repeat(address.getColumn() - 1) + WattColors.ANSI_RED + "^" + WattColors.ANSI_RESET);
        System.out.println();
        System.out.println(WattColors.ANSI_YELLOW + "hint: " + WattColors.ANSI_RESET + hint);
        System.out.print(WattColors.ANSI_RESET);
        System.exit(errorCode());
    }

    @Override
    public int errorCode() {
        return 5;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int address() {
        return address.getLine();
    }
}
