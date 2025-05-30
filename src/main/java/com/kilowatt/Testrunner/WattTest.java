package com.kilowatt.Testrunner;

import com.kilowatt.Executor.WattExecutor;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

/*
 тест Watt
 */
@Builder
@Getter
public class WattTest {
    // информация о тесте
    private String name;
    private String path;
    private List<String> args;

    // запуск теста
    public Exception run() {
        try {
            WattExecutor.test(path);
            return null;
        } catch (IOException | RuntimeException e) {
            return e;
        }
    }
}
