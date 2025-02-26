package com.kea.Compiler.Builtins.Libraries;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Instructions.VmInstructionCondOp;
import com.kea.KeaVM.VmAddress;

import java.util.HashMap;

/*
Словари
 */
public class KeaMap {
    private final HashMap<Object, Object> map = new HashMap<Object, Object>();

    public void set(VmAddress address, Object k, Object v) {
        map.put(k,v);
    }

    public void del(VmAddress address, Object k) {
        for (Object o : map.keySet()) {
            if (VmInstructionCondOp.equal(address, k, o)) {
                map.remove(o);
            }
        }
    }

    public Object stringify(VmAddress address) {
        return map.toString();
    }

    public Object get(VmAddress address, Object k) {
        for (Object o : map.keySet()) {
            if (VmInstructionCondOp.equal(address, k, o)) {
                return map.get(o);
            }
        }
        throw new KeaRuntimeError(
                address.getLine(), address.getFileName(),
                "Key: " + k.toString() + " not found!", "Check key for mistakes!");
    }

    public Object has_key(VmAddress address, Object obj) {
        for (Object o : map.keySet()) {
            if (VmInstructionCondOp.equal(address, o, obj)) {
                return true;
            }
        }

        return false;
    }

    public Object has_value(VmAddress address, Object obj) {
        for (Object o : map.values()) {
            if (VmInstructionCondOp.equal(address, o, obj)) {
                return true;
            }
        }

        return false;
    }

    public KeaList keys(VmAddress address) {
        return KeaList.of(map.keySet().stream().toList());
    }

    public KeaList values(VmAddress address) {
        return KeaList.of(map.values().stream().toList());
    }
    public float size(VmAddress address) {
        return map.size();
    }
}
