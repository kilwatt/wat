package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Инстанс класса ВМ
 */
@Getter
public class VmInstance implements VmFunctionOwner {
    // класс
    private final VmType type;
    // скоуп
    private final VmFrame<String, Object> fields = new VmFrame<>();
    // адрес
    private final VmAddress addr;

    // конструктор
    public VmInstance(WattVM vm, VmType type, VmAddress addr)  {
        this.type = type;
        this.addr = addr;
        for (int i = type.getConstructor().size()-1; i >= 0; i--) {
            Object arg = vm.pop();
            fields.define(addr, type.getConstructor().get(i), arg);
        }
        fields.setRoot(vm.getGlobals());
        type.getBody().run(vm, fields);
        if (fields.has("init")) {
            call(addr, "init", vm, false);
        }
    }

    /**
     * Вызов функции объекта
     * @param name - имя функции
     * @param vm - ВМ
     */
    public void call(VmAddress inAddr, String name, WattVM vm, boolean shouldPushResult)  {
        // копируем и вызываем функцию
        VmFunction fun = (VmFunction) getFields().lookup(inAddr, name);
        fun.exec(vm, shouldPushResult, this);
    }

    // в строку

    @Override
    public String toString() {
        return "VmInstance(" +
                "fields=" + fields.getValues().keySet() +
                ", type=" + type.getName() +
                ", addr=" + addr +
                ')';
    }

    // получение локального скоупа
    @Override
    public VmFrame<String, Object> getLocalScope() {
        return fields;
    }
}
