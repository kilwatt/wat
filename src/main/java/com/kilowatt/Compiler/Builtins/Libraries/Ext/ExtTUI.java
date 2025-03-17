package com.kilowatt.Compiler.Builtins.Libraries.Ext;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/*
TUI фрэймворк
 */
@Getter
public class ExtTUI {
    private final Terminal terminal;

    public ExtTUI() {
        try {
            terminal = TerminalBuilder.terminal();
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in tui: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    /*
    Функции рендера
     */
    public void render_line(String line) {
        terminal.writer().println(line);
    }

    public void render(String line) {
        terminal.writer().print(line);
    }

    /*
    Функции очистки
     */
    public void clear() {
        terminal.writer().print("\033[H\033[2J");
    }

    /*
    Хэндлеры
     */
    public int read_key() {
        terminal.writer().flush();
        try {
            return terminal.reader().read();
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in tui: " + e.getMessage(),
                    "check your code."
            );
        }
    }
}
