package com.kilowatt.Errors;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Reflection.VmCallInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Ошбика рантайма
 */
@Getter
@AllArgsConstructor
public class WattRuntimeError extends WattError {
    private final int line;
    private final String filename;
    private final String message;
    private final String hint;

    @Override
    public void panic() {
        System.out.print(WattColors.ANSI_RED);
        System.out.println("╭ ⚡ runtime error.");
        System.out.println("│ err: " + this.message);
        System.out.println("│ at: " + filename + "::" + line);
        System.out.println("│ 💡 " + hint);
        System.out.println("│———————————————");
        System.out.println("│ calls trace: ");
        for (VmCallInfo element : WattCompiler.vm.getCallsHistory().reversed()) {
            System.out.println("│ " + element);
        }
        System.out.println("╰");
        System.out.print(WattColors.ANSI_RESET);
        System.exit(errorCode());
    }

    @Override
    public int errorCode() {
        return 4;
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
