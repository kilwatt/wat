package com.kea.KeaVM.Entities;

import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void call(VmAddress inAddr, String name, KeaVM vm, boolean shouldPushResult)  {
        // копируем и вызываем функцию
        VmFunction func = (VmFunction) fields.lookup(inAddr, name);
        func.setDefinedFor(this);
        func.exec(vm, shouldPushResult);
    }
}
