package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Кастомный тип VM
 */
@Getter
public class VmUnit implements VmFunctionOwner {
    private final String name;
    private final VmFrame<String, Object> fields;

    public VmUnit(String name, VmFrame<String, Object> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public VmFrame<String, Object> getLocalScope() {
        return fields;
    }

    @Override
    public String toString() {
        return "VmUnit(" +
                "name='" + name + '\'' +
                ", fields=" + fields +
                ')';
    }

    /**
     * Вызов функции юнита
     * @param name - имя функции
     * @param vm - ВМ
     */
    public void call(VmAddress inAddr, String name, WattVM vm, boolean shouldPushResult)  {
        // копируем и вызываем функцию
        VmFunction fun = (VmFunction) fields.lookup(inAddr, name);
        fun.setDefinedFor(this);
        fun.exec(vm, shouldPushResult);
    }
}
