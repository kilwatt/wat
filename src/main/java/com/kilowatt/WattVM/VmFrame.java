package com.kilowatt.WattVM;

import com.kilowatt.Errors.WattRuntimeError;
import lombok.Getter;

import java.util.HashMap;

/*
Фрейм стека - дженерик, являющийся
хранилищем для ВМ
 */
@SuppressWarnings("StringTemplateMigration")
@Getter
public class VmFrame<K, V> {
    // значения для хранения
    private HashMap<K, V> values = new HashMap<>();
    /* рутовый фрейм, предназначен для поиска
       в случае отсутствия в текущем фрейме переменной.
       выглядит в виде иерархии:
       функци -> класс -> глобал
     */
    private VmFrame<K, V> root;

    /**
     * Ищет значение в фрейме
     * @param addr - адрес
     * @param name - имя значения
     * @return возвращает значение
     */
    public V lookup(VmAddress addr, K name) {
        VmFrame<K, V> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                throw new WattRuntimeError(
                        addr.getLine(),
                        addr.getFileName(),
                        "not found: " + name.toString(),
                        "check variable existence!"
                );
            }
            current = current.root;
        }
        return current.getValues().get(name);
    }

    /**
     * Устанавливает значение в фрейм, учитывая предыдущие
     * @param addr - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void set(VmAddress addr, K name, V val) {
        VmFrame<K, V> current = this;

        while (current != null) {
            if (current.getValues().containsKey(name)) {
                current.getValues().put(name, val);
                return;
            }
            current = current.root;
        }

        throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                "variable is not defined: " + name, "verify you already defined it with := op.");
    }

    /**
     * Устанавливает значение в фрейм, не учитывая предыдущие
     * @param addr - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void define(VmAddress addr, K name, V val) {
        if (!getValues().containsKey(name)) {
            getValues().put(name, val);
        }
        else {
            throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "variable: " + name + " already defined!", "you can rename variable to define it.");
        }
    }

    /**
     * Возвращает бул показывающий
     * на то, найдено ли во фрейме
     * @return - найдено ли (бул)
     */
    public boolean has(K name) {
        VmFrame<K, V> current = this;

        while (current != null) {
            if (current.getValues().containsKey(name)) {
                return true;
            }
            current = current.root;
        }

        return false;
    }

    /**
     * Установка рут фрейма, если у
     * этого фрейма уже есть рут,
     * то ставиться рут для рут... и тд.
     * @param rootFrame - фрейм
     */
    public void setRoot(VmFrame<K, V> rootFrame) {
        if (this.root == rootFrame) { return; }
        VmFrame<K, V> current = this;
        while (current.getRoot() != null) {
            if (current.getRoot() == rootFrame) { return; }
            current = current.getRoot();
        }
        current.root = rootFrame;
    }

    /**
     * Удаленние фрейма
     */
    public void delRoot() {
        root = null;
    }

    // в строку
    @Override
    public String toString() {
        return "VmFrame{" +
                "values=" + values +
                ", root=" + root +
                '}';
    }

    /*
    Копирование
     */
    public VmFrame<K, V> copy() {
        VmFrame<K, V> copied = new VmFrame<>();
        copied.values = new HashMap<>(getValues());
        return copied;
    }
}
