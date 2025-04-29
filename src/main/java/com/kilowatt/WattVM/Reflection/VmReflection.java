package com.kilowatt.WattVM.Reflection;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Рефлексия в ВМ
 */
@SuppressWarnings("rawtypes")
@Getter
@AllArgsConstructor
public class VmReflection {
    // вм
    private final WattVM vm;

    // рефлексия
    @SneakyThrows
    public Object reflect(String name, WattList args) {
        // адрес
        VmAddress address = vm.getCallsHistory().getLast().getAddress();
        // ищем класс
        try {
            // класс
            Class<?> clazz = VmJvmClasses.lookup(address, name);
            // колличество аргументов
            int argsAmount = args.size();
            // ищем конструктор
            Constructor constructor = findConstructor(
                address,
                clazz,
                argsAmount,
                args.getArray().stream()
                    .map(Object::getClass)
                    .toArray(Class[]::new)
            );
            // создаём экземпляр
            return constructor.newInstance(castJvmArgs(constructor, args.getArray()));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // ошибки
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            if (cause instanceof WattRuntimeError
                    || cause instanceof WattParsingError) {
                throw cause;
            } else {
                throw new WattRuntimeError(address.getLine(), address.getFileName(),
                        "error in jvm constructor: " + e.getMessage(), "check your code.");
            }
        }
    }

    // загрузка класса
    public void define(Path path) {
        // имя класса
        String className = path.getFileName().toString().replace(".class", "");
        // загрузка
        try {
            // загружаем класс
            // чтение файла
            byte[] classData = Files.readAllBytes(path);
            // дефайн класса
            Class<?> clazz = VmJvmClasses.classLoader.define(className, classData);
            // дефайним класс
            VmJvmClasses.define(clazz);
        } catch (IOException e) {
            // адрес
            VmAddress address = vm.getCallsHistory().getLast().getAddress();
            // оишбка
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "couldn't define class " + className + ", " + e.getMessage(),
                "check your code."
            );
        }
    }

    /*
    Каст аргументов
     */
    private Object[] castJvmArgs(Constructor constructor, List<Object> objects) {
        ArrayList<Object> jvmArgs = new ArrayList<>();
        for (int c = 0; c < constructor.getParameterCount(); c++) {
            Class<?> clazz = constructor.getParameterTypes()[c];
            jvmArgs.add(clazz.cast(objects.get(c)));
        }
        return jvmArgs.toArray();
    }

    /*
    Сравнение аргументов
     */
    private boolean checkParameters(Class<?>[] expected, Class<?>[] passed) {
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].isAssignableFrom(passed[i])) {
                return false;
            }
        }

        return true;
    }

    /*
    Поиск конструктора
     */
    private Constructor findConstructor(VmAddress address, Class<?> clazz,
            int argsAmount, Class<?>[] parameterTypes) {
        // ищем конструктор
        for (Constructor c : clazz.getConstructors()) {
            if (c.getParameterCount() == argsAmount &&
                checkParameters(c.getParameterTypes(), parameterTypes)
            ) {
                return c;
            }
        }
        // в ином случае, ошибка
        throw new WattRuntimeError(
                address.getLine(), address.getFileName(),
                "constructor with args: "
                        + Arrays.toString(parameterTypes) + " for: " +
                        clazz.getSimpleName() + " is not found.",
                clazz.getSimpleName()
        );
    }
}
