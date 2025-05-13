package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

/*
Кастомный тип VM
 */
@Getter
public class VmUnit implements VmFunctionOwner {
    // имена
    private final String name;
    private final String fullName;
    // адрес
    private final VmAddress address;
    // поля
    private final VmFrame<String, Object> fields = new VmFrame<>();

    // конструктор
    public VmUnit(VmAddress address, String name, String fullName) {
        // данные
        this.name = name;
        this.fullName = fullName;
        this.address = address;
    }

    @Override
    public String toString() {
        return "VmUnit(" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", fields=" + fields.getValues().keySet() +
                ", address=" + address +
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
    public void call(VmAddress address, String name, WattVM vm, boolean shouldPushResult)  {
        // ищем функцию
        Object val = getFields().lookup(address, name);
        // проверяем, функция ли
        if (val instanceof VmFunction fn) {
            fn.exec(vm, shouldPushResult);
        } else {
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "couldn't call: " + name + ", not a fn.",
                "check your code"
            );
        }
    }
}
