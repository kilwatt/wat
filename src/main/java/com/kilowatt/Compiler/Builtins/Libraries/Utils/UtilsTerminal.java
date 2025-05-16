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
            // Создаем терминал
            terminal = TerminalBuilder.builder()
                .system(true)
                .build();
        } catch (IOException e) {
            // адрес
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            // ошибка
            throw new WattRuntimeError(
                address,
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
                    address,
                    "io error, while starting jline terminal: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    // очистка консоли
    public void clear() {
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
    }

    // очистка линии
    public void clear_line() {
        terminal.writer().println("\033[2K");
    }

    // очистка символа
    public void clear_ch() {
        terminal.writer().println("\b \b");
    }

    // установка позиции курсора
    public void set_cursor(int x, int y) {
        terminal.puts(InfoCmp.Capability.cursor_address, x, y);
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
