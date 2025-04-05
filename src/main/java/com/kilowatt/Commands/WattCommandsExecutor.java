package com.kilowatt.Commands;

import com.kilowatt.Errors.WattColors;
import com.kilowatt.Executor.WattExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/*
Хэндлер команд
 */
@SuppressWarnings("ConstantValue")
public class WattCommandsExecutor {
    // комманды
    private final HashMap<String, WattCommand> commands = new HashMap<>(){{
        put("-v", new WattVersionCommand());
        put("-tests", new WattTestsCommand());
        put("-d", new WattDumpCommand());
        put("-df", new WattDumpFileCommand());
    }};

    // вывод информацию об использовании
    public void usage() {
        error(WattColors.ANSI_YELLOW +
            "Invalid usage. Usages:" +
            "\n........" +
            "\nwatt <script> :: runs script" +
            "\nwatt -v :: info about watt version" +
            "\nwatt -tests :: run internal watt tests" +
            "\nwatt -d <script> :: dumps bytecode" +
            "\nwatt -df <script> :: dumps bytecode to file" +
            "\n........" +
            WattColors.ANSI_RESET
        );
    }

    private String[] sliceArgs(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public void exec(String[] args) throws IOException {
        // команда
        String command = args[0];
        // аргументы команды
        String[] commandArgs = sliceArgs(args);
        // перебор команд
        if (args.length >= 1) {
            // если есть такая команда
            if (commands.containsKey(command)) {
                commands.get(command).execute(commandArgs);
            }
            // если нет - то воспринимаем как запуск скрипта
            else {
                // запуск
                WattExecutor.run(args[0], commandArgs);
            }
        } else {
            // информация об использовании
            usage();
        }
    }

    // вывод ошибки
    public void error(String warning) {
        // вывод
        System.out.println(warning);
        // выход
        System.exit(-1);
    }
}
