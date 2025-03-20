package com.kilowatt.Compiler.Builtins.Libraries.Std.Sys;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/*
Стд -> Система
 */
public class StdSys {
    // инфа о системе
    private final SystemInfo info = new SystemInfo();

    // получение параметра
    public String get_property(String name) {
        return System.getProperty(name);
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
}
