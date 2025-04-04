package com.kilowatt.WattVM.Reflection;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
Рефлексия в ВМ
 */
@SuppressWarnings("rawtypes")
@Getter
@AllArgsConstructor
public class VmReflection {
    // вм
    private final WattVM vm;
    // инфа о последнем вызове
    private final ThreadLocal<VmCallInfo> lastCallInfo = new ThreadLocal<>();

    // работа с инфой о последнем вызове
    public VmCallInfo getLastCallInfo() {
        return lastCallInfo.get();
    }

    public void setLastCallInfo(VmCallInfo callInfo) {
        lastCallInfo.set(callInfo);
    }

    // рефлексия
    @SneakyThrows
    public Object reflect(String name, WattList args) {
        // ищем класс
        try {
            // класс
            Class<?> clazz = VmJvmClasses.lookup(getLastCallInfo().getAddress(), name);
            // колличество аргументов
            int argsAmount = args.size();
            // ищем конструктор
            Constructor constructor = findConstructor(getLastCallInfo().getAddress(), clazz, argsAmount);
            return constructor.newInstance(args.getArray().toArray());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            if (cause instanceof WattRuntimeError
                    || cause instanceof WattParsingError) {
                throw cause;
            } else {
                throw new WattRuntimeError(getLastCallInfo().getAddress().getLine(), getLastCallInfo().getAddress().getFileName(),
                        "error in jvm constructor: " + e.getMessage(), "check your code.");
            }
        }
    }

    /*
    Поиск конструктора
     */
    private Constructor findConstructor(VmAddress addr, Class<?> clazz, int argsAmount) {
        for (Constructor c : clazz.getConstructors()) {
            if (c.getParameterCount() == argsAmount) {
                return c;
            }
        }

        throw new WattRuntimeError(
                addr.getLine(), addr.getFileName(),
                "constructor with args amount: "
                        + argsAmount + " not found.",
                clazz.getSimpleName()
        );
    }
}
