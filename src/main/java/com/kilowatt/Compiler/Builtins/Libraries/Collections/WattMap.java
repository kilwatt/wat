package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import com.kilowatt.WattVM.VmAddress;

import java.util.HashMap;

/*
Словари
 */
public class WattMap {
    private final HashMap<Object, Object> map = new HashMap<>();

    public void set(Object k, Object v) {
        map.put(k,v);
    }

    public void del(Object k) {
        for (Object o : map.keySet()) {
            if (VmInstructionCondOp.equal(
                    WattCompiler.vm.getCallsHistory().getLast().getAddress(),
                    k,
                    o
            )) {
                map.remove(o);
            }
        }
    }

    public Object to_string() {
        return map.toString();
    }

    public Object get(Object k) {
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        for (Object o : map.keySet()) {
            if (VmInstructionCondOp.equal(address, k, o)) {
                return map.get(o);
            }
        }
        throw new WattRuntimeError(
                address.getLine(), address.getFileName(),
                "key: " + k.toString() + " is not exists.", "check key for mistakes.");
    }

    public Object has_key(Object obj) {
        for (Object o : map.keySet()) {
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

    public Object has_value(Object obj) {
        for (Object o : map.values()) {
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

    public WattList keys() {
        return WattList.of(map.keySet().stream().toList());
    }

    public WattList values() {
        return WattList.of(map.values().stream().toList());
    }
    public float size() {
        return map.size();
    }
}
