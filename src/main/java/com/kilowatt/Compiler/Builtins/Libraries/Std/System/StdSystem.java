package com.kilowatt.Compiler.Builtins.Libraries.Std.System;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.Builtins.Libraries.Std.Fs.FsPath;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.Executor.WattExecutor;
import com.kilowatt.WattVM.VmAddress;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/*
Стд -> Система
 */
public class StdSystem {
    // инфа о системе
    private final SystemInfo info = new SystemInfo();

    // получение параметра
    public String get_property(String name) {
        return System.getProperty(name);
    }
    
    // установка значения для локальной переменной системы
    public void set_property(String name, String value) {
        System.setProperty(name, value);
    }

    // получение рантайма
    public Runtime get_runtime() {
        return Runtime.getRuntime();
    }

    // получение информации о системе
    public SystemInfo get_info() {
        return info;
    }

    // получение hal
    public HardwareAbstractionLayer get_hal() {
        return info.getHardware();
    }

    // получение cwd
    public FsPath cwd() {
        return new FsPath(Path.of("").toAbsolutePath());
    }

    // выход из выполнения
    public void exit(int code) {
        System.exit(code);
    }

    // получение аргументов
    public WattList args() {
        return WattList.of(WattExecutor.getPassedArgs());
    }

    // получение значения переменной среды
    public Object get_env(String name) {
        return System.getenv(name);
    }

    // старт процесса
    public SystemProcess process(WattList args, boolean shouldRedirect) {
        // билд процесса
        String[] processArgs = convertProcessArgs(args);
        ProcessBuilder builder = new ProcessBuilder(processArgs);
        // редайрект 'сообщений' процесса
        if (shouldRedirect) {
            builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        }
        // запуск
        try {
            return new SystemProcess(builder.inheritIO().start());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "io error in process(" + Arrays.toString(processArgs) + "): " + e.getMessage(),
                "check your commands list"
            );
        }
    }

    // конвертация аргументов процесса
    private String[] convertProcessArgs(WattList args) {
        try {
            String[] arr = new String[args.getList().size()];
            for (int i = 0; i < args.size(); i++) {
                arr[i] = args.get(i).toString();
            }
            return arr;
        } catch (RuntimeException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "invalid process args: " + args + ", error: " + e.getMessage(),
                "args must be list of strings."
            );
        }
    }
}
