package com.kilowatt.Compiler.Builtins.Libraries.Utils;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import lombok.Getter;

import java.io.IOException;

/*
RAW мод
 */
@Getter
public class UtilsTerminal {
    // стандартный termios
    private final TerminalJNA.Termios termios;
    // для выключения равмода
    private final TerminalJNA.Termios originAttributes;

    /*
    Jna интерфейс, для взаимодействия с TermioS
     */
    @SuppressWarnings("SpellCheckingInspection")
    private interface TerminalJNA extends Library {
        // параметры
        int SYSTEM_OUT_FD = 0;
        int ISIG = 1;
        int ICANON = 2;
        int ECHO = 10;
        int TCSAFLUSH = 2;
        int IXON = 2000;
        int ICRNL = 400;
        int IEXTEN = 100000;
        int OPOST = 1;
        int VMIN = 6;
        int VTIME = 5;
        int TIOCGWINSZ = 0x5413;

        // инстанс
        TerminalJNA instance = Native.load("c", TerminalJNA.class);

        /*
        Термиос
         */
        @Structure.FieldOrder(value = {"c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_cc"})
        class Termios extends Structure {
            public int c_iflag, c_oflag, c_cflag, c_lflag;
            public byte[] c_cc = new byte[19];

            public Termios() {
            }

            public static Termios of(Termios termios) {
                Termios copy = new Termios();
                copy.c_iflag = termios.c_iflag;
                copy.c_oflag = termios.c_oflag;
                copy.c_cflag = termios.c_cflag;
                copy.c_lflag = termios.c_lflag;
                return copy;
            }
        }

        /*
        Остальное
         */
        int tcgetattr(int fd, Termios termios);

        int tcsetattr(int fd, int optional_actions, Termios termios);
    }

    public UtilsTerminal() {
        termios = new TerminalJNA.Termios();
        int errorCode = TerminalJNA.instance.tcgetattr(TerminalJNA.SYSTEM_OUT_FD, termios);
        if (errorCode != 0) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "unexpected error with termios. error code (" + errorCode + ")",
                    "check your code."
            );
        } else {
            originAttributes = TerminalJNA.Termios.of(termios);
        }
    }

    public void enter_raw_mode() {
        termios.c_lflag &= ~(TerminalJNA.ECHO | TerminalJNA.ICANON | TerminalJNA.IEXTEN | TerminalJNA.ISIG);
        termios.c_iflag &= ~(TerminalJNA.IXON | TerminalJNA.ICRNL);
        termios.c_oflag &= ~(TerminalJNA.OPOST);
        termios.c_cc[TerminalJNA.VMIN] = 0;
        termios.c_cc[TerminalJNA.VTIME] = 1;
        TerminalJNA.instance.tcsetattr(TerminalJNA.SYSTEM_OUT_FD, TerminalJNA.TCSAFLUSH, termios);
    }

    public void exit_raw_mode() {
        TerminalJNA.instance.tcsetattr(TerminalJNA.SYSTEM_OUT_FD, TerminalJNA.TCSAFLUSH, originAttributes);
    }

    public String read_key() {
        try {
            char key = (char) System.in.read();
            return String.valueOf(key);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io error, while reading key: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    public void clear() {
        System.out.println("\033[2J");
        System.out.println("\033[H");
    }

    public void clear_line() {
        System.out.println("\033[2K");
    }

    public void print(String value) {
        System.out.print(value);
    }

    public void println(String value) {
        System.out.println(value);
    }
}
