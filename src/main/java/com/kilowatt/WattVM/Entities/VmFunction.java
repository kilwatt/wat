package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/*
Функция вм
 */
@SuppressWarnings("UnnecessaryReturnStatement")
@Getter
@RequiredArgsConstructor
public class VmFunction implements VmBindableFunction {
    // имя функции
    private final String name;
    // полное имя функции
    private final String fullName;
    // инструкции
    private final VmChunk body;
    // параметры
    private final ArrayList<String> params;
    // адрес
    private final VmAddress address;
    // замыкание
    private VmTable<String, Object> closure = null;
    // бинд к объекту
    @Setter
    private VmFunctionOwner selfBind;

    /**
     * Выполнение функции
     * @param vm - ВМ
     * @param shouldPushResult - положить ли результат в стек
     */
    public void exec(WattVM vm, boolean shouldPushResult)  {
        // новый фрэйм
        VmTable<String, Object> table = new VmTable<>();
        // замыкание
        if (getClosure() != null) {
            table.setClosure(closure);
        }
        // устанавливаем рут скоупа и переменную self
        if (selfBind != null) {
            table.setRoot(selfBind.getFields());
            table.define(address, "self", selfBind);
        } else {
            table.setRoot(vm.getGlobals());
        }
        // аргументы
        loadArgs(vm, table);
        // инструкции
        try {
            // исполняем функцию
            body.run(vm, table);
        } catch (VmReturnValue returnable) {
            if (shouldPushResult) {
                vm.push(returnable.getObject());
            }
            return;
        }
    }

    /**
     * Загрузка аргументов в функции
     */
    private void loadArgs(WattVM vm, VmTable<String, Object> scope) {
        // загружаем аргументы
        for (int i = params.size()-1; i >= 0; i--) {
            if (vm.getStack().isEmpty()) {
                throw new WattRuntimeError(
                    address,
                    "stack is empty! couldn't invoke function.",
                    "check args amount."
                );
            }
            Object arg = vm.pop(address);
            scope.define(address, params.get(i), arg);
        }
    }

    // установка замыкания
    public void setClosure(VmTable<String, Object> closure) {
        // устанавливаем замыкание функции
        if (this.getClosure() == null) {
            this.closure = closure;
        }
    }

    // копия функции
    public VmFunction copy() {
        // возвращаем
        return new VmFunction(name, fullName, body, params, address);
    }

    // в строку
    @Override
    public String toString() {
        return "VmFunction(" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address=" + address +
                ", closure=" + (closure == null ? "no" : closure.getValues().keySet()) +
                ')';
    }
}
