package com.kilowatt.Compiler.Builtins.Libraries.Utils;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/*
RAW мод
 */
@Getter
public class UtilsTerminal {
    private final Terminal terminal;

    public UtilsTerminal() {
        try {
            // Создаем терминал
            terminal = TerminalBuilder.builder()
                .system(true)
                .build();
        } catch (IOException e) {
            // адрес
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            // ошибка
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "io error, while starting jline terminal: " + e.getMessage(),
                "check your code."
            );
        }
    }

    // рав мод
    public void enter_raw_mode() {
        terminal.enterRawMode();
    }

    // чтение клавиши
    public String read_key() {
        try {
            return String.valueOf((char) terminal.reader().read());
        } catch (IOException e) {
            // адрес
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            // ошибка
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io error, while starting jline terminal: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    // очистка консоли
    public void clear() {
        System.out.println("\033[2J");
        System.out.println("\033[H");
    }

    // очистка линии
    public void clear_line() {
        System.out.println("\033[2K");
    }

    /*
    Принты
     */

    public void print(String value) {
        System.out.print(value);
    }

    public void println(String value) {
        System.out.println(value);
    }
}
