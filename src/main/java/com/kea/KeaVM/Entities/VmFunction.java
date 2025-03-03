package com.kea.KeaVM.Entities;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.Instructions.VmInstructionReturn;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
Функция вм
 */
@SuppressWarnings("UnnecessaryReturnStatement")
@Getter
public class VmFunction implements VmInstructionsBox {
    // имя функции
    private final String name;
    // инструкции
    private List<VmInstruction> instructions = new ArrayList<>();
    // аргументы
    private final ArrayList<String> arguments;
    // владелец функций
    @Setter
    private VmFunctionOwner definedFor;
    // адрес
    private final VmAddress addr;
    // замыкание
    private final ThreadLocal<VmFrame<String, Object>> closure = new ThreadLocal<>();

    // конструкция
    public VmFunction(String name, ArrayList<String> arguments, VmAddress addr) {
        this.name = name;
        this.arguments = arguments;
        this.addr = addr;
    }

    /**
     * Выполнение функции
     * @param vm - ВМ
     * @param shouldPushResult - положить ли результат в стек
     */
    public void exec(KeaVM vm, boolean shouldPushResult)  {
        VmFrame<String, Object> scope = new VmFrame<>();
        if (definedFor != null) {
            scope.setRoot(definedFor.getLocalScope());
        } else {
            scope.setRoot(vm.getGlobals());
        }
        if (getClosure().get() != null) {
            scope.getValues().putAll(closure.get().getValues());
        }
        loadArgs(vm, scope);
        if (definedFor != null) {
            scope.define(addr, "self", definedFor);
        }
        try {
            // исполняем функцию
            for (VmInstruction instr : instructions) {
                instr.run(vm, scope);
            }
        } catch (VmInstructionReturn e) {
            e.pushResult(vm, scope);
            if (!shouldPushResult) {
                vm.pop();
            }
            return;
        }
    }

    /**
     * Загрузка аргументов в функции
     */
    private void loadArgs(KeaVM vm, VmFrame<String, Object> scope) {
        // загружаем аргументы
        for (int i = arguments.size()-1; i >= 0; i--) {
            if (vm.getStack().isEmpty()) {
                throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                        "Stack is empty! Can't invoke functions.",
                        "Check arguments for function!");
            }
            Object arg = vm.pop();
            scope.define(addr, arguments.get(i), arg);
        }
    }

    /**
     Копия функции
     @return возвращает копию
     */
    public VmFunction copy() {
        VmFunction fn = new VmFunction(name, arguments, addr);
        fn.instructions = instructions;
        return fn;
    }

    /**
     * Добавляет инструкцию
     * @param instr - инструкция
     */
    @Override
    public void visitInstr(VmInstruction instr) {
        this.instructions.add(instr);
    }

    // замыкание в строку
    private final String closureString() {
        if (closure.get() == null)
        {
            return "null";
        }
        else{
            return String.valueOf(closure.get());
        }
    }

    // установка замыкания
    public void setClosure(VmFrame<String, Object> closure) {
        // удаляем из замыкания
        closure.getValues().remove(this.getName());
        // устанавливаем замыкание
        if (this.closure.get() == null) {
            this.closure.set(closure);
        } else {
            this.closure.get().setRoot(closure);
        }
    }

    // в строку
    @Override
    public String toString() {
        return "VmFunction{" +
                "name='" + name + '\'' +
                ", addr=" + addr +
                ", definedFor=" + definedFor +
                ", closure=" + closureString() +
                '}';
    }
}
