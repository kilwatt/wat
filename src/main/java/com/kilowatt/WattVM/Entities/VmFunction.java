package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstructionReturn;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
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
public class VmFunction {
    // имя функции
    private final String name;
    // инструкции
    private final VmChunk body;
    // параметры
    private final ArrayList<String> params;
    // адрес
    private final VmAddress address;
    // замыкание
    private VmFrame<String, Object> closure = null;
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
        VmFrame<String, Object> scope = new VmFrame<>();
        // замыкание
        if (getClosure() != null) {
            scope.setClosure(closure);
        }
        // устанавливаем рут скоупа и переменную self
        if (selfBind != null) {
            scope.setRoot(selfBind.getLocalScope());
            scope.define(address, "self", selfBind);
        } else {
            scope.setRoot(vm.getGlobals());
        }
        // аргументы
        loadArgs(vm, scope);
        // инструкции
        try {
            // исполняем функцию
            body.run(vm, scope);
        } catch (VmInstructionReturn e) {
            e.pushResult(vm, scope);
            if (!shouldPushResult) {
                vm.pop(address);
            }
            return;
        }
    }

    /**
     * Загрузка аргументов в функции
     */
    private void loadArgs(WattVM vm, VmFrame<String, Object> scope) {
        // загружаем аргументы
        for (int i = params.size()-1; i >= 0; i--) {
            if (vm.getStack().isEmpty()) {
                throw new WattRuntimeError(address.getLine(), address.getFileName(),
                "stack is empty! couldn't invoke function.",
                "check args amount.");
            }
            Object arg = vm.pop(address);
            scope.define(address, params.get(i), arg);
        }
    }

    // установка замыкания
    public void setClosure(VmFrame<String, Object> closure) {
        // устанавливаем замыкание функции
        if (this.getClosure() == null) {
            this.closure = closure;
        }
    }

    // копия функции
    public VmFunction copy() {
        // возвращаем
        return new VmFunction(name, body, params, address);
    }

    // в строку
    @Override
    public String toString() {
        return "VmFunction(" +
                "name='" + name + '\'' +
                ", addr=" + address +
                ", closure=" + (closure == null ? "no" : closure.getValues().keySet()) +
                ')';
    }
}
