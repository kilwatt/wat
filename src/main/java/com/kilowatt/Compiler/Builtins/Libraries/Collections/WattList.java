package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Списки
 */
@Getter
public class WattList {
    private List<Object> array = new ArrayList<>();

    public void add(Object o) {
        array.add(o);
    }
    public void delete_at(int index) {
        array.remove(index);
    }
    public void delete(Object v) {
        array.remove(v);
    }
    public Object get(int index) {
        return array.get(index);
    }
    public Object contains(Object obj) {
        for (Object o : array) {
            if (VmInstructionCondOp.equal(
                    WattCompiler.vm.getCallsHistory().getLast().getAddress(),
                    o,
                    obj
            )) {
                return true;
            }
        }

        return false;
    }
    public void set(int i, Object v) {
        array.set(i, v);
    }
    public int size() {
        return array.size();
    }
    public Object to_string() {
        return this;
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < array.size(); i++) {
            // получаем по индексу
            Object o = array.get(i);
            // форматирум
            if (i + 1 == array.size()) {
                s.append(o);
            } else {
                s.append(o).append(",");
            }
        }
        s.append("]");
        return s.toString();
    }
    public void insert(int i, Object v) {
        array.add(i, v);
    }
    public int index_of(Object obj) {
        for (Object o : array) {
            if (VmInstructionCondOp.equal(
                    WattCompiler.vm.getCallsHistory().getLast().getAddress(),
                    o,
                    obj
            )) {
                return array.indexOf(o);
            }
        }

        return -1;
    }
    public WattIterator<Object> iter() {
        return new WattIterator<>(this.array.iterator());
    }
    public static WattList of(List<Object> values) {
        WattList arr = new WattList();
        arr.array = values;
        return arr;
    }
    public static WattList of(Object[] values) {
        WattList arr = new WattList();
        arr.array = List.of(values);
        return arr;
    }
    public void del_all(WattList arr) {
        this.array.removeAll(arr.array);
    }
    public WattList copy() {
        return WattList.of(array);
    }
}
