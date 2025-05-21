package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import lombok.AllArgsConstructor;

import java.util.Iterator;

/*
Обёртка над итератором
 */
@AllArgsConstructor
public class WattIterator<T> {
    // итератор
    private final Iterator<T> iterable;

    // функции
    public boolean has_next() {
        return iterable.hasNext();
    }
    public T next() {
        return iterable.next();
    }
    public boolean equals(WattIterator<T> other) { return this.iterable.equals(other.iterable); }
    public String to_string() { return this.toString(); }
    @Override
    public String toString() { return iterable.toString(); }
}
