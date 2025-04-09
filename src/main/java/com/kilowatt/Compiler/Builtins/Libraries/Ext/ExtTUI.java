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
            VmAddress address = WattCompiler.vm.getCallsTrace().getLast().getAddress();
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
    public void render_line(Object line) {
        terminal.writer().println(line);
        terminal.writer().flush();
    }

    public void render(Object line) {
        terminal.writer().print(line);
        terminal.writer().flush();
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
            VmAddress address = WattCompiler.vm.getCallsTrace().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in tui: " + e.getMessage(),
                    "check your code."
            );
        }
    }
}
