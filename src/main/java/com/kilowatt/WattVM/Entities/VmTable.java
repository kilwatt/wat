package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

/*
Фрейм стека - дженерик, являющийся
хранилищем для ВМ
 */
@SuppressWarnings({"StringTemplateMigration"})
@Getter
public class VmTable<K, V> {
    // значения для хранения
    private final ConcurrentHashMap<K, V> values = new ConcurrentHashMap<>();
    /* рутовый фрейм, предназначен для поиска
       в случае отсутствия в текущем фрейме переменной.
       выглядит в виде иерархии:
       функци -> класс -> глобал
     */
    private VmTable<K, V> root;
    // замыкание
    @Setter
    private VmTable<K, V> closure;

    /**
     * Содержится ли объект в этом фрэйме или
     * в замыкании
     * @param name - имя значения
     * @return да или нет
     */
    private boolean existsInFrameOrClosure(K name) {
        return (values.containsKey(name)) ||
                (closure != null && closure.has(name));
    }

    /**
     * Ищет значение в таблице, и рутах
     * @param address - адрес
     * @param name - имя значения
     * @return возвращает значение
     */
    public V lookup(VmAddress address, K name) {
        VmTable<K, V> current = this;
        // фрэймы
        while (!current.existsInFrameOrClosure(name)) {
            if (current.root == null) {
                throw new WattRuntimeError(
                    address,
                    "not found: " + name.toString(),
                    "check variable existence!"
                );
            }
            current = current.root;
        }
        // проверяем
        if (current.values.containsKey(name)) return current.values.get(name);
        else return current.closure.lookup(address, name);
    }

    /**
     * Ищет значение ТОЛЬКО в этой таблице
     * @param address - адрес
     * @param name - имя значения
     * @return возвращает значение
     */
    public V find(VmAddress address, K name) {
        // возвращаем
        if (values.containsKey(name)) return values.get(name);
        else throw new WattRuntimeError(
            address,
            "not found: " + name,
            "check variables existence"
        );
    }

    /**
     * Устанавливает значение в фрейм, учитывая предыдущие
     * @param address - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void set(VmAddress address, K name, V val) {
        VmTable<K, V> current = this;
        // фрэймы
        while (current != null) {
            // проверка фрэйма
            if (current.values.containsKey(name)) {
                current.values.put(name, val);
                return;
            }
            // проверка замыкания фрэйма
            else if (current.closure != null && current.closure.has(name)) {
                current.closure.set(address, name, val);
                return;
            }
            // прыгаем в рут
            current = current.root;
        }
        // ошибка
        throw new WattRuntimeError(address,
                "variable is not defined: " + name, "verify you already defined it with := op.");
    }

    /**
     * Устанавливает значение в фрейм принудительно
     * @param address - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void forceSet(VmAddress address, K name, V val) {
        values.put(name, val);
    }

    /**
     * Устанавливает значение в фрейм, не учитывая предыдущие
     * @param address - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void define(VmAddress address, K name, V val) {
        if (!values.containsKey(name)) {
            values.put(name, val);
        }
        else {
            throw new WattRuntimeError(address,
                    "variable: " + name + " already defined!", "you can rename variable to define it.");
        }
    }

    /**
     * Возвращает бул показывающий
     * на то, найдено ли во фрейме
     * @return - найдено ли (бул)
     */
    public boolean has(K name) {
        VmTable<K, V> current = this;
        // фрэймы
        while (current != null) {
            if (current.existsInFrameOrClosure(name)) {
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
     * <p>
     * Рут не ставиться в случае если он уже есть
     * в иерархии рутов.
     * </p>
     * @param rootFrame - фрейм
     */
    public void setRoot(VmTable<K, V> rootFrame) {
        // проверка на цикличную зависимость
        if (this.root == rootFrame || this == rootFrame) { return; }
        // ищем объект с пустым рутом
        VmTable<K, V> current = this;
        while (current.root != null) {
            if (current.root == rootFrame || current == rootFrame) { return; }
            current = current.root;
        }
        // устанавливаем рут
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
}