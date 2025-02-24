package com.kea.KeaVM.Entities;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Инстанс класса ВМ
 */
@Getter
public class VmInstance {
    // скоуп
    private final VmFrame<String, Object> scope = new VmFrame<>();
    // класс
    private final VmType type;
    // адрес
    private final VmAddress addr;

    // конструктор
    public VmInstance(KeaVM vm, VmType type, VmAddress addr)  {
        this.type = type;
        this.addr = addr;
        for (int i = type.getConstructor().size()-1; i >= 0; i--) {
            Object arg = vm.pop();
            scope.define(addr, type.getConstructor().get(i), arg);
        }
        scope.setRoot(vm.getGlobals());
        type.getBody().execWithoutPop(vm, scope);
        if (scope.has("init")) {
            call(addr, "init", vm, false);
        }
    }

    /**
     * Вызов функции объекта
     * @param name - имя функции
     * @param vm - ВМ
     */
    public void call(VmAddress inAddr, String name, KeaVM vm, boolean shouldPushResult)  {
        // копируем и вызываем функцию
        VmFunction func = (VmFunction) getScope().lookup(addr, name);
        func.setDefinedFor(this);
        func.exec(vm, shouldPushResult);
    }

    // в строку

    @Override
    public String toString() {
        return "VmObj{" +
                "scope=" + scope +
                ", clazz=" + type +
                ", addr=" + addr +
                '}';
    }
}
