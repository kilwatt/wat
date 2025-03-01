package com.kea.Compiler.Builtins.Libraries;

import com.kea.KeaVM.Instructions.VmInstructionCondOp;
import com.kea.KeaVM.VmAddress;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Списки
 */
@Getter
public class KeaList {
    private List<Object> array = new ArrayList<>();

    public void add(VmAddress address, Object o) {
        array.add(o);
    }

    public void del(VmAddress address, int index) {
        array.remove(index);
    }

    public Object get(VmAddress address, int index) {
        return array.get(index);
    }

    public Object contains(VmAddress address, Object obj) {
        for (Object o : array) {
            if (VmInstructionCondOp.equal(address, o, obj)) {
                return true;
            }
        }

        return false;
    }

    public void set(VmAddress address, int i, Object v) {
        array.set(i, v);
    }

    public int size(VmAddress address) {
        return array.size();
    }

    public Object stringify(VmAddress address) {
        StringBuilder s = new StringBuilder();
        for (Object o : array) {
            s.append(o);
        }
        return s.toString();
    }
    public void insert(VmAddress address, int i, Object v) {
        array.add(i, v);
    }

    public int index_of(VmAddress address, Object obj) {
        for (Object o : array) {
            if (VmInstructionCondOp.equal(address, o, obj)) {
                return array.indexOf(o);
            }
        }

        return -1;
    }
    public static KeaList of(List<Object> values) {
        KeaList arr = new KeaList();
        arr.array = values;
        return arr;
    }
    public void del_all(VmAddress address, KeaList arr) {
        this.array.removeAll(arr.array);
    }
}
