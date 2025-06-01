package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParseError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.*;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Reflection.VmCallInfo;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/*
Вызов функции в VM
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class VmInstructionCall implements VmInstruction {
    // адресс
    private final VmAddress address;
    // имя
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // аргументы
    private final VmChunk args;
    // выключен ли пуш
    private final boolean shouldPushResult;

    // конструктор
    public VmInstructionCall(VmAddress address, String name, VmChunk args,
                             boolean hasPrevious, boolean shouldPushResult) {
        this.address = address;
        this.name = name; this.args = args; this.hasPrevious = hasPrevious;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table)  {
        // устанавливаем инфу о последнем вызове
        WattCompiler.vm.getCallsTrace().add(new VmCallInfo(address, name, table));
        // вызов
        if (!hasPrevious) {
            callGlobalFunc(vm, table);
        } else {
            Object last = vm.pop(address);
            if (last instanceof VmInstance vmInstance) {
                callInstanceFunc(vm, table, vmInstance);
            } else if (last instanceof VmUnit vmUnit){
                callUnitFunc(vm, table, vmUnit);
            } else {
                callReflectionFunc(vm, table, last);
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "CALL("+name+", SP: " + shouldPushResult + ")");
        VmCodeDumper.dumpLine(indent + 1, "ARGS:");
        for (VmInstruction instruction : args.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    // Вызывает функцю объекта
    private void callInstanceFunc(WattVM vm, VmTable<String, Object> table, VmInstance instance)  {
        // аргументы и поиск функции
        int argsAmount = passArgs(vm, table);
        Object val = instance.getFields().lookupLocal(address, name);
        // функция
        if (val instanceof VmFunction fn) {
            checkArgs(instance.getType().getName() + ":" + name, fn.getParams().size(), argsAmount);
            // вызов
            instance.call(address, name, vm, shouldPushResult);
        }
        // нативная функция
        else if (val instanceof VmBuiltinFunction fn) {
            checkArgs(instance.getType().getName() + ":" + name, fn.paramsAmount(), argsAmount);
            // вызов
            fn.exec(vm, address, shouldPushResult);
        }
        // в ином случае - ошибка
        else {
            throw new WattRuntimeError(address,
                "couldn't call: " + name + ", not a fn.",
                "check your code.");
        }
    }

    // Вызывает функцю юнита
    private void callUnitFunc(WattVM vm, VmTable<String, Object> table, VmUnit unit)  {
        // аргументы и поиск функции
        int argsAmount = passArgs(vm, table);
        Object val = unit.getFields().lookupLocal(address, name);
        // функция
        if (val instanceof VmFunction fn) {
            checkArgs(unit.getName() + ":" + name, fn.getParams().size(), argsAmount);
            // вызов
            unit.call(address, name, vm, shouldPushResult);
        }
        // нативная функция
        else if (val instanceof VmBuiltinFunction fn) {
            checkArgs(unit.getName() + ":" + name, fn.paramsAmount(), argsAmount);
            // вызов
            fn.exec(vm, address, shouldPushResult);
        }
        // в ином случае - ошибка
        else {
            throw new WattRuntimeError(address,
                "couldn't call: " + name + ", not a fn.",
                "check your code.");
        }
    }

    // Вызывает рефлексийную функцию
    @SneakyThrows
    private void callReflectionFunc(WattVM vm, VmTable<String, Object> table, Object last) {
        // аргументы
        int argsAmount = passArgs(vm, table);
        Object[] callArgs = toJvmArgs(vm, argsAmount);
        // поиск метода
        Method fun = vm.getReflection().findMethod(
                address, last.getClass(), name,
                argsAmount, Arrays.stream(callArgs)
                    .map(Object::getClass)
                    .toArray(Class[]::new)
        );
        fun.setAccessible(true);
        // вызов
        try {
            // 👇 ВОЗВРАЩАЕТ NULL, ЕСЛИ ФУНКЦИЯ НИЧЕГО НЕ ВОЗВРАЩАЕТ
            Object returned = fun.invoke(last, callArgs);
            if (shouldPushResult) {
                vm.push(returned);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new WattRuntimeError(
                address,
                "jvm call error (" + name + "): " + e.getMessage(), "check your code."
            );
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof WattRuntimeError ||
                    e.getCause() instanceof WattParseError) {
                throw e.getCause();
            } else {
                String message = e.getCause().getMessage() != null ?
                        e.getCause().getMessage() : e.getCause().getClass().getSimpleName();
                throw new WattRuntimeError(
                    address,
                    "jvm call error (" + name + "): " + message, "check your code."
                );
            }
        }
    }

    // Преобразование в аргументы для jvm метода.
    private Object[] toJvmArgs(WattVM vm, int argsAmount) {
        // создаём список аргументов.
        Object[] callArgs = new Object[argsAmount];
        // заполняем его
        for (int i = argsAmount - 1; i >= 0; i--) {
            callArgs[i] = vm.pop(address);
        }
        // возвращаем аргументы
        return callArgs;
    }

    // Вызов функции из глобального скоупа
    private void callGlobalFunc(WattVM vm, VmTable<String, Object> table)  {
        // аргументы
        int argsAmount = passArgs(vm, table);
        Object o = table.lookup(address, name);
        // функция
        if (o instanceof VmFunction fn) {
            checkArgs(fn.getName(), fn.getParams().size(), argsAmount);
            fn.exec(vm, shouldPushResult);
        }
        // нативная функция
        else if (o instanceof VmBuiltinFunction fn) {
            checkArgs(fn.getName(), fn.paramsAmount(), argsAmount);
            fn.exec(vm, address, shouldPushResult);
        }
        // в ином случае - ошибка
        else {
            throw new WattRuntimeError(address,
                "couldn't call: " + name + ", not a fn.",
                "check your code.");
        }
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(String name, int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            throw new WattRuntimeError(address,
                "invalid args amount for call: "
                        + name + "(" + argsAmount + "/" + parameterAmount + ")",
                "check passing args amount.");
        }
    }

    // помещает аргументы в стек
    private int passArgs(WattVM vm, VmTable<String, Object> table)  {
        int size = vm.getStack().size();
        args.run(vm, table);
        return vm.getStack().size()-size;
    }

    @Override
    public String toString() {
        return "CALL_FUNCTION(" + name + ",instrs:" + args.getInstructions().size() + ")";
    }
}
