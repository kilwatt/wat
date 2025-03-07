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
    public Object reflect(VmAddress address, String name, WattList args) {
        // ищем класс
        try {
            // класс
            Class<?> clazz = VmJvmClasses.lookup(address, name);
            // колличество аргументов
            int argsAmount = args.size(address);
            // ищем конструктор
            Constructor constructor = findConstructor(address, clazz, argsAmount);
            return constructor.newInstance(args.getArray().toArray());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new WattRuntimeError(address.getLine(), address.getFileName(),
                    "error in jvm constructor: " + e.getMessage(), "check your code.");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof WattRuntimeError ||
                e.getCause() instanceof WattParsingError) {
                throw e.getCause();
            } else {
                throw new WattRuntimeError(address.getLine(), address.getFileName(),
                        "error in jvm constructor: " + e.getMessage(), "check your code.");
            }
        }
    }

    /*
    Поиск конструктора
     */
    private Constructor findConstructor(VmAddress addr, Class<?> clazz, int argsAmount) {
        Constructor constructor = null;
        for (Constructor c : clazz.getConstructors()) {
            if (c.getParameterCount() == argsAmount) {
                constructor = c;
            }
        }
        if (constructor == null) {
            throw new WattRuntimeError(
                    addr.getLine(), addr.getFileName(),
                    "constructor with args amount: "
                            + argsAmount + " not found.",
                    clazz.getSimpleName()
            );
        }

        return constructor;
    }
}
