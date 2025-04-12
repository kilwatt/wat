package com.kilowatt.WattVM;

import com.kilowatt.Errors.WattRuntimeError;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

/*
Фрейм стека - дженерик, являющийся
хранилищем для ВМ
 */
@SuppressWarnings({"StringTemplateMigration"})
@Getter
public class VmFrame<K, V> {
    // значения для хранения
    private final ConcurrentHashMap<K, V> values = new ConcurrentHashMap<>();
    /* рутовый фрейм, предназначен для поиска
       в случае отсутствия в текущем фрейме переменной.
       выглядит в виде иерархии:
       функци -> класс -> глобал
     */
    private VmFrame<K, V> root;
    // замыкание
    @Setter
    private VmFrame<K, V> closure;

    /**
     * Содержится ли объект в этом фрэйме или
     * в замыкании
     * @param name - имя значения
     * @return да или нет
     */
    private boolean existsInFrameOrClosure(K name) {
        return (getValues().containsKey(name)) ||
                (getClosure() != null && getClosure().has(name));
    }

    /**
     * Ищет значение в фрейме
     * @param addr - адрес
     * @param name - имя значения
     * @return возвращает значение
     */
    public V lookup(VmAddress addr, K name) {
        VmFrame<K, V> current = this;
        // фрэймы
        while (!current.existsInFrameOrClosure(name)) {
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
        // проверяем
        if (current.getValues().containsKey(name)) return current.getValues().get(name);
        else return current.getClosure().lookup(addr, name);
    }

    /**
     * Устанавливает значение в фрейм, учитывая предыдущие
     * @param addr - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void set(VmAddress addr, K name, V val) {
        VmFrame<K, V> current = this;
        // фрэймы
        while (current != null) {
            // проверка фрэйма
            if (current.getValues().containsKey(name)) {
                current.getValues().put(name, val);
                return;
            }
            // проверка замыкания фрэйма
            else if (current.getClosure() != null && current.getClosure().has(name)) {
                current.getClosure().set(addr, name, val);
                return;
            }
            // прыгаем в рут
            current = current.root;
        }
        // ошибка
        throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                "variable is not defined: " + name, "verify you already defined it with := op.");
    }

    /**
     * Устанавливает значение в фрейм принудительно
     * @param addr - адрес
     * @param name - имя значения
     * @param val - значение
     */
    public void forceSet(VmAddress addr, K name, V val) {
        values.put(name, val);
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
    public void setRoot(VmFrame<K, V> rootFrame) {
        // проверка на цикличную зависимость
        if (this.root == rootFrame || this == rootFrame) { return; }
        // ищем объект с пустым рутом
        VmFrame<K, V> current = this;
        while (current.getRoot() != null) {
            if (current.getRoot() == rootFrame || current == rootFrame) { return; }
            current = current.getRoot();
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