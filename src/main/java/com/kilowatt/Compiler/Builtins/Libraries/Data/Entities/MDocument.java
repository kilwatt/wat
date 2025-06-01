package com.kilowatt.Compiler.Builtins.Libraries.Data.Entities;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

/*
Документ
 */
@AllArgsConstructor
@Getter
public class MDocument {
    // мапа
    private final Document document;

    // функции
    public void set(String k, Object v) {
        document.put(k,v);
    }
    public void delete(String k) {
        document.remove(k);
    }
    public Object to_string() {
        return this;
    }
    @Override
    public String toString() {
        return document.toString();
    }
    public Object get(String k) {
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        if (document.containsKey(k)) {
            return document.get(k);
        }
        throw new WattRuntimeError(
                address,
                "key: " + k + " is not exists.",
                "check key for mistakes."
        );
    }
    public boolean has_key(String k) {
        return document.containsKey(k);
    }
    public boolean has_value(Object obj) {
        return document.containsValue(obj);
    }
    public WattList keys() {
        return WattList.of(document.keySet().stream().map(Object.class::cast).toList());
    }
    public WattList values() {
        return WattList.of(document.values().stream().toList());
    }
    public WattList pairs() {
        return WattList.of(document.entrySet().toArray());
    }
    public float size() {
        return document.size();
    }
    public boolean equals(Document other) {
        return this.document.equals(other);
    }
}
