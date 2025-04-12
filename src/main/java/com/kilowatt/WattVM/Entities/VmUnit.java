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
    Бинды функций к юниту
     */
    public void bindFunctionsToUnit() {
        // Фильтруются функции, которые не привязаны
        // к конкретному юниту или экземпляру типа. В последствии
        // они привязываются к этому юниту.
        fields.getValues().values().stream()
            .filter(field -> field instanceof VmFunction fn)
            .map(field -> (VmFunction) field)
            .filter(fn -> fn.getSelfBind() == null)
            .forEach(fn -> fn.setSelfBind(this));
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
