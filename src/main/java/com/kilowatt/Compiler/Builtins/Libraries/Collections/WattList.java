package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
Списки
 */
@Getter
public class WattList {
    // список
    private List<Object> list = new ArrayList<>();

    // функции
    public void add(Object o) {
        list.add(o);
    }
    public void add_all(WattList another) { this.list.addAll(another.list); }
    public void delete_at(int index) {
        list.remove(index);
    }
    public void delete(Object v) {
        list.remove(v);
    }
    public Object get(int index) {
        return list.get(index);
    }
    public Object contains(Object obj) {
        for (Object o : list) {
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
        list.set(i, v);
    }
    public int size() {
        return list.size();
    }
    public Object to_string() {
        return this;
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            // получаем по индексу
            Object o = list.get(i);
            // форматирум
            if (i + 1 == list.size()) {
                s.append(o);
            } else {
                s.append(o).append(",");
            }
        }
        s.append("]");
        return s.toString();
    }
    public void insert(int i, Object v) {
        list.add(i, v);
    }
    public int index_of(Object obj) {
        for (Object o : list) {
            if (VmInstructionCondOp.equal(
                    WattCompiler.vm.getCallsHistory().getLast().getAddress(),
                    o,
                    obj
            )) {
                return list.indexOf(o);
            }
        }

        return -1;
    }
    public WattIterator<Object> iter() {
        return new WattIterator<>(this.list.iterator());
    }
    public static WattList of(List<Object> values) {
        WattList arr = new WattList();
        arr.list = values;
        return arr;
    }
    public static WattList of(Object[] values) {
        WattList arr = new WattList();
        arr.list = new ArrayList<>(List.of(values));
        return arr;
    }
    public void add_java(Object values) {
        if (values instanceof ArrayList<?> another) {
            this.list.addAll(another);
        } else if (values.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(values); i++) {
                list.add(Array.get(values, i));
            }
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "couldn't use add_java with " + values.getClass().getSimpleName(),
                "can be used with: ArrayList<?>, ?[]"
            );
        }
    }
    public void del_all(WattList arr) {
        this.list.removeAll(arr.list);
    }
    public WattList copy() {
        return WattList.of(list);
    }
    public boolean equals(WattList other) {
        return this.list.equals(other.list);
    }
}
