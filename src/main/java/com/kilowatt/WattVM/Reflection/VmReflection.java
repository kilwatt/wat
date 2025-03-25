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
    private final WattVM vm;

    // рефлексия
    @SneakyThrows
    public Object reflect(String name, WattList args) {
        // ищем класс
        try {
            // класс
            Class<?> clazz = VmJvmClasses.lookup(vm.getLastCallAddress(), name);
            // колличество аргументов
            int argsAmount = args.size();
            // ищем конструктор
            Constructor constructor = findConstructor(vm.getLastCallAddress(), clazz, argsAmount);
            return constructor.newInstance(args.getArray().toArray());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            if (cause instanceof WattRuntimeError
                    || cause instanceof WattParsingError) {
                throw cause;
            } else {
                throw new WattRuntimeError(vm.getLastCallAddress().getLine(), vm.getLastCallAddress().getFileName(),
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
