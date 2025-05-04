package com.kilowatt.Compiler.Builtins.Libraries.Utils;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

/*
RAW мод
 */
@Getter
public class UtilsTerminal {
    private final Terminal terminal;

    public UtilsTerminal() {
        try {
            terminal = TerminalBuilder.terminal();
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in terminal: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    // вход в рав мод
    public void enter_raw_mode() {
        terminal.enterRawMode();
    }

    /*
    Функции рендера
     */
    public void println(Object line) {
        terminal.writer().println(line);
        terminal.writer().flush();
    }

    public void print(Object line) {
        terminal.writer().print(line);
        terminal.writer().flush();
    }

    /*
    Функции очистки
     */
    public void clear() {
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
    }
    public void clear_line() {
        terminal.writer().print("\r\033[K");
        terminal.flush();
    }

    /*
    Чтение клавиши
     */
    public int read_key() {
        try {
            terminal.writer().flush();
            return terminal.reader().read();
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in tui: " + e.getMessage(),
                    "check your code."
            );
        }
    }
}
