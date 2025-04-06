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
    // имена
    private final String name;
    private final String fullName;
    // поля
    private final VmFrame<String, Object> fields = new VmFrame<>();

    // конструктор
    public VmUnit(String name, String fullName) {
        // данные
        this.name = name;
        this.fullName = fullName;
        // бинды функций
        bindFunctions();
    }

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
    Бинды функций
     */
    private void bindFunctions() {
        for (Object field : fields.getValues().values()) {
            if (field instanceof VmFunction fn && fn.getBind() == null) {
                fn.setBind(this);
            }
        }
    }

    /**
     * Вызов функции юнита
     * @param name - имя функции
     * @param vm - ВМ
     */
    public void call(VmAddress inAddr, String name, WattVM vm, boolean shouldPushResult)  {
        // копируем и вызываем функцию
        VmFunction fun = (VmFunction) fields.lookup(inAddr, name);
        fun.exec(vm, shouldPushResult);
    }
}
