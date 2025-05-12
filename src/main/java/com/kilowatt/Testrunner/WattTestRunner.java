package com.kilowatt.Testrunner;

import com.kilowatt.Errors.WattColors;
import com.kilowatt.Errors.WattError;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Тест раннер
 */
@Getter
public class WattTestRunner {
    private final ArrayList<WattTest> tests = new ArrayList<>();
    public WattTestRunner(WattTest... tests) {
        this.tests.addAll(List.of(tests));
    }
    // запуск тестов
    public void run() {
        // зелёный цвет
        log(WattColors.ANSI_LIME);
        // выводим информацию
        logLine("[Running tests]");
        System.out.println();
        for (int i = 0; i < tests.size(); i++) {
            // выполняем тест
            WattTest test = tests.get(i);
            Exception e = test.run();
            if (e == null) {
                logLine(
                         WattColors.ANSI_RESET + "[" + (i + 1) + "/" + tests.size() + "]: " + test.getName()
                                 + WattColors.ANSI_LIME + " | success." + WattColors.ANSI_RESET
                );
            } else {
                log(WattColors.ANSI_RED);
                if (e instanceof WattError wattError) {
                    logLine(
                            WattColors.ANSI_RESET + "[" + (i + 1) + "/" + tests.size() + "]: " + test.getName()
                                    + WattColors.ANSI_RED + " | error: " + wattError.message() + " at "
                                    + wattError.address() + WattColors.ANSI_RESET
                    );
                } else {
                    logLine(
                            WattColors.ANSI_RESET + "[" + (i + 1) + "/" + tests.size() + "]: " + test.getName()
                                    + WattColors.ANSI_RED + " | error: " + e + WattColors.ANSI_RESET
                    );
                }
                log(WattColors.ANSI_GREEN);
            }
        }
        // вырубаем цвета
        log(WattColors.ANSI_RESET);
    }

    // логи теста
    private void log(Object value) {
        System.out.print(value);
    }

    // логи теста
    private void logLine(Object value) {
        System.out.println(value);
    }
}
