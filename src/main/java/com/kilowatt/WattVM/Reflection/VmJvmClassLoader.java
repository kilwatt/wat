package com.kilowatt.WattVM.Reflection;

/*
Класс лоадер для JVM классов
 */
public class VmJvmClassLoader extends ClassLoader {
     public Class<?> define(String name, byte[] data) {
         return this.defineClass(name, data, 0, data.length);
     }
}
