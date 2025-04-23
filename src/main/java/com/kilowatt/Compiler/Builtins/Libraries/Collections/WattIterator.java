package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import lombok.AllArgsConstructor;

import java.util.Iterator;

/*
Обёртка над итератором
 */
@AllArgsConstructor
public class WattIterator<T> {
    private final Iterator<T> iterable;

    public boolean has_next() {
        return iterable.hasNext();
    }

    public T next() {
        return iterable.next();
    }
}
