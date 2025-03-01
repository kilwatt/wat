package com.kea.KeaVM.Reflection;

import com.kea.Compiler.Builtins.Libraries.KeaList;
import com.kea.Errors.KeaParsingError;
import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/*
Рефлексия в ВМ
 */
@SuppressWarnings("rawtypes")
@Getter
@AllArgsConstructor
public class VmReflection {
    private final KeaVM vm;

    // рефлексия
    @SneakyThrows
    public Object reflect(VmAddress address, String name, KeaList args) {
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
            throw new KeaRuntimeError(address.getLine(), address.getFileName(),
                    "Error while calling jvm constructor: " + e.getMessage(), "Check your code!");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof KeaRuntimeError ||
                e.getCause() instanceof KeaParsingError) {
                throw e.getCause();
            } else {
                throw new KeaRuntimeError(address.getLine(), address.getFileName(),
                        "Error while calling jvm constructor: " + e.getMessage(), "Check your code!");
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
            throw new KeaRuntimeError(
                    addr.getLine(), addr.getFileName(),
                    "constructor with args amount: "
                            + argsAmount + " not found.",
                    clazz.getSimpleName()
            );
        }

        return constructor;
    }
}
