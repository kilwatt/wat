package com.kea.KeaVM;

import com.kea.Errors.KeaRuntimeError;
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
                throw new KeaRuntimeError(addr.getLine(), addr.getFileName(), "Not found: " + name.toString(), "Check this variable existence!");
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
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                break;
            }
            current = current.root;
        }
        if (current.getValues().containsKey(name)) {
            current.getValues().put(name, val);
        }
        else if (getValues().containsKey(name)) {
            current.getValues().put(name, val);
        }
        else {
            throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Variable: " + name + " is not defined!", "Verify you already defined it with := operator.");
        }
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
            throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Variable: " + name + " already defined!", "You can rename this variable!");
        }
    }

    /**
     * Возвращает бул показывающий
     * на то, найдено ли во фрейме
     * @return - найдено ли (бул)
     */
    public boolean has(K name) {
        VmFrame<K, V> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                return false;
            }
            current = current.root;
        }
        return current.getValues().containsKey(name);
    }

    /**
     * Установка рут фрейма, если у
     * этого фрейма уже есть рут,
     * то ставиться рут для рут... и тд.
     * @param rootFrame - фрейм
     */
    public void setRoot(VmFrame<K, V> rootFrame) {
        VmFrame<K, V> current = this;
        if (this.root == rootFrame) { return; }
        while (current.getRoot() != null) {
            if (current.getRoot() == rootFrame) { return; }
            current = current.getRoot();
        }
        current.root = rootFrame;
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
        VmFrame<K, V> copied = new VmFrame<K, V>();
        copied.values = new HashMap<>(getValues());
        return copied;
    }
}
