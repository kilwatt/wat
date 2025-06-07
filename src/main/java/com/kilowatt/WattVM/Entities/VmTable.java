package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

/*
Таблице - дженерик, являющийся
хранилищем для ВМ
 */
@SuppressWarnings({"StringTemplateMigration"})
@Getter
public class VmTable<K, V> {
    // значения для хранения
    private final ConcurrentHashMap<K, V> values = new ConcurrentHashMap<>();
    /* рутовая таблица, предназначена для поиска
       в случае отсутствия в текущей таблице переменной.
       выглядит в виде иерархии:
       функция -> класс -> глобал
     */
    private VmTable<K, V> root;
    // замыкание
    @Setter
    private VmTable<K, V> closure;

    /**
     * Содержится ли объект в этой таблице или
     * в замыкании
     * @param name - имя значения
     * @return да или нет
     */
    private boolean existsLocal(K name) {
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
        // текущая таблица
        VmTable<K, V> current = this;
        // таблицы
        while (!current.existsLocal(name)) {
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
    public V lookupLocal(VmAddress address, K name) {
        // возвращаем
        if (values.containsKey(name)) return values.get(name);
        else throw new WattRuntimeError(
            address,
            "not found: " + name,
            "check variables existence"
        );
    }

    /**
     * Устанавливает значение в таблице, учитывая верхние
     * таблицы
     * @param address - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void set(VmAddress address, K name, V val) {
        VmTable<K, V> current = this;
        // проверяем таблицы
        while (current != null) {
            // проверка таблицы
            if (current.values.containsKey(name)) {
                current.values.put(name, val);
                return;
            }
            // проверка замыкания таблицы
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
     * Устанавливает значение в таблице,
     * не учитывая верхние таблицы
     * @param address - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void setLocal(VmAddress address, K name, V val) {
        // проверка таблицы
        if (values.containsKey(name)) {
            values.put(name, val);
            return;
        }
        // ошибка
        throw new WattRuntimeError(address,
                "variable is not defined: " + name, "verify you already defined it with := op.");
    }

    /**
     * Устанавливает значение в таблицу
     * принудительно
     * @param address - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void forceSet(VmAddress address, K name, V val) {
        values.put(name, val);
    }

    /**
     * Устанавливает значение в таблице,
     * не учитывая предыдущие таблицы
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
     * на то, найдено ли в этой таблице
     * или верхних значение с именем name
     * @param name - имя
     * @return - найдено ли (бул)
     */
    public boolean has(K name) {
        // текущая таблица
        VmTable<K, V> current = this;
        // таблицы
        while (current != null) {
            if (current.existsLocal(name)) {
                return true;
            }
            current = current.root;
        }

        return false;
    }

    /**
     * Установка рут таблицы, если у
     * этой таблицы уже есть рут,
     * то ставиться рут для рут... и тд.
     * <p>
     * Рут не ставиться в случае если он уже есть
     * в иерархии рутов.
     * </p>
     * @param rootTable - таблица
     */
    public void setRoot(VmTable<K, V> rootTable) {
        // проверка на цикличную зависимость
        if (this.root == rootTable || this == rootTable) { return; }
        // ищем объект с пустым рутом
        VmTable<K, V> current = this;
        while (current.root != null) {
            if (current.root == rootTable || current == rootTable) { return; }
            current = current.root;
        }
        // устанавливаем рут
        current.root = rootTable;
    }

    /**
     * Удаленние рут таблицы
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