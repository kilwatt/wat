package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import com.kilowatt.WattVM.VmAddress;

import java.util.HashMap;

/*
Словари
 */
public class WattMap {
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
        throw new WattRuntimeError(
                address.getLine(), address.getFileName(),
                "key: " + k.toString() + " is not exists.", "check key for mistakes.");
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

    public WattList keys(VmAddress address) {
        return WattList.of(map.keySet().stream().toList());
    }

    public WattList values(VmAddress address) {
        return WattList.of(map.values().stream().toList());
    }
    public float size(VmAddress address) {
        return map.size();
    }
}
