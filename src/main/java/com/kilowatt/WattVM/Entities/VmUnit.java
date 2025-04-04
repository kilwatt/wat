package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Кастомный тип VM
 */
@Getter
@AllArgsConstructor
public class VmUnit implements VmFunctionOwner {
    private final String name;
    private final String fullName;
    private final VmFrame<String, Object> fields = new VmFrame<>();

    @Override
    public VmFrame<String, Object> getLocalScope() {
        return fields;
    }

    @Override
    public String toString() {
        return "VmUnit(" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", fields=" + fields.getValues().keySet() +
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
        fun.exec(vm, shouldPushResult, this);
    }
}
