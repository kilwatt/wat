package com.kilowatt.WattVM.Reflection;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.util.ArrayList;

/*
Jvm-классы для получения
в ВМ
 */
public class VmJvmClasses {
    // список классов
    private static final ArrayList<Class<?>> defined = new ArrayList<>();

    /**
     * Получения класса. Если класс
     * не найден в дефайнутых юзером,
     * ищем в стандартном класс лоадере
     * @param address - адресс
     * @param name - имя
     * @return класс
     */
    public static Class<?> lookup(VmAddress address, String name) {
        for (Class<?> clazz : defined) {
            if (clazz.getName().equals(name)) {
                return clazz;
            }
        }
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new WattRuntimeError(address.getLine(), address.getFileName(),
                    "jvm class is not defined: " + name, "check class name for mistakes.");
        }
    }

    /**
     * дефайн класса
     * @param clazz - класс для дефайна
     */
    public static void define(Class<?> clazz) {
        defined.add(clazz);
    }
}
