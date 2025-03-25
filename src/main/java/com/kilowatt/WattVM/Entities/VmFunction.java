package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Boxes.VmInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.Instructions.VmInstructionReturn;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    public void exec(WattVM vm, boolean shouldPushResult)  {
        VmFrame<String, Object> scope = new VmFrame<>();
        if (getClosure().get() != null) {
            scope.setRoot(closure.get());
        }
        if (definedFor != null) {
            scope.setRoot(definedFor.getLocalScope());
            scope.define(addr, "self", definedFor);
        } else {
            scope.setRoot(vm.getGlobals());
        }
        loadArgs(vm, scope);
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
    private void loadArgs(WattVM vm, VmFrame<String, Object> scope) {
        // загружаем аргументы
        for (int i = arguments.size()-1; i >= 0; i--) {
            if (vm.getStack().isEmpty()) {
                throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                        "stack is empty! couldn't invoke function.",
                        "check args of function.");
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
        // устанавливаем замыкание функции
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
