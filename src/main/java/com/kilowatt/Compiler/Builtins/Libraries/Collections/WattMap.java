package com.kilowatt.Compiler.Builtins.Libraries.Collections;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/*
Мапа (словарь)
 */
@Getter
public class WattMap {
    // мапа
    private Map<Object, Object> map = new HashMap<>();

    // функции
    public void set(Object k, Object v) {
        map.put(k,v);
    }
    public void delete(Object k) {
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
        return this;
    }
    @Override
    public String toString() {
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
            address,
            "key: " + k.toString() + " is not exists.",
            "check key for mistakes."
        );
    }
    public boolean has_key(Object obj) {
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
    public boolean has_value(Object obj) {
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
    public WattList pairs() {
        return WattList.of(map.entrySet().toArray());
    }
    public float size() {
        return map.size();
    }
    public boolean equals(WattMap other) {
        return this.map.equals(other.map);
    }
    @SuppressWarnings("unchecked")
    public static <K, V> WattMap of(Map<K, V> map) {
        WattMap wattMap = new WattMap();
        wattMap.map = (Map<Object, Object>) map;
        return wattMap;
    }
}
