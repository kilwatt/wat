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

    public void del(int index) {
        array.remove(index);
    }

    public Object get(int index) {
        return array.get(index);
    }

    public Object contains(Object obj) {
        for (Object o : array) {
            if (VmInstructionCondOp.equal(
                    WattCompiler.vm.getLastCallAddress(),
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

    public Object stringify() {
        StringBuilder s = new StringBuilder();
        for (Object o : array) {
            s.append(o).append(", ");
        }
        return s.toString();
    }
    public void insert(int i, Object v) {
        array.add(i, v);
    }

    public int index_of(Object obj) {
        for (Object o : array) {
            if (VmInstructionCondOp.equal(
                    WattCompiler.vm.getLastCallAddress(),
                    o,
                    obj
            )) {
                return array.indexOf(o);
            }
        }

        return -1;
    }
    public static WattList of(List<Object> values) {
        WattList arr = new WattList();
        arr.array = values;
        return arr;
    }
    public void del_all(WattList arr) {
        this.array.removeAll(arr.array);
    }
}
