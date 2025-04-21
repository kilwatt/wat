package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.*;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmNull;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Reflection.VmCallInfo;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/*
Вызов функции в VM
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class VmInstructionCall implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // имя
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // аргументы
    private final VmBaseInstructionsBox args;
    // выключен ли пуш
    private final boolean shouldPushResult;

    // конструктор
    public VmInstructionCall(VmAddress addr, String name, VmBaseInstructionsBox args,
                             boolean hasPrevious, boolean shouldPushResult) {
        this.addr = addr;
        this.name = name; this.args = args; this.hasPrevious = hasPrevious;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // устанавливаем инфу о последнем вызове
        WattCompiler.vm.getCallsTrace().add(new VmCallInfo(addr, name, frame));
        // вызов
        if (!hasPrevious) {
            callGlobalFunc(vm, frame);
        } else {
            Object last = vm.pop(addr);
            if (last instanceof VmInstance vmInstance) {
                callInstanceFunc(vm, frame, vmInstance);
            } else if (last instanceof VmUnit vmUnit){
                callUnitFunc(vm, frame, vmUnit);
            } else {
                callReflectionFunc(vm, frame, last);
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "CALL("+name+", SP: " + shouldPushResult + ")");
        VmCodeDumper.dumpLine(indent + 1, "ARGS:");
        for (VmInstruction instruction : args.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    // Вызывает функцю объекта
    private void callInstanceFunc(WattVM vm, VmFrame<String, Object> frame, VmInstance vmObj)  {
        // аргументы и поиск функции
        int argsAmount = passArgs(vm, frame);
        Object val = vmObj.getFields().lookup(addr, name);
        // функция
        if (val instanceof VmFunction fn) {
            checkArgs(vmObj.getType().getName() + ":" + name, fn.getArguments().size(), argsAmount);
            // вызов
            vmObj.call(addr, name, vm, shouldPushResult);
        }
        // нативная функция
        else if (val instanceof VmBuiltinFunction fn) {
            checkArgs(vmObj.getType().getName() + ":" + name, fn.args(), argsAmount);
            // вызов
            fn.exec(vm, addr, shouldPushResult);
        }
    }

    // Вызывает функцю юнита
    private void callUnitFunc(WattVM vm, VmFrame<String, Object> frame, VmUnit vmUnit)  {
        // аргументы и поиск функции
        int argsAmount = passArgs(vm, frame);
        Object val = vmUnit.getFields().lookup(addr, name);
        // функция
        if (val instanceof VmFunction fn) {
            checkArgs(vmUnit.getName() + ":" + name, fn.getArguments().size(), argsAmount);
            // вызов
            vmUnit.call(addr, name, vm, shouldPushResult);
        }
        // нативная функция
        else if (val instanceof VmBuiltinFunction fn) {
            checkArgs(vmUnit.getName() + ":" + name, fn.args(), argsAmount);
            // вызов
            fn.exec(vm, addr, shouldPushResult);
        }
    }

    // Вызывает рефлексийную функцию
    @SneakyThrows
    private void callReflectionFunc(WattVM vm, VmFrame<String, Object> frame, Object last) {
        // аргументы
        int argsAmount = passArgs(vm, frame);
        Object[] callArgs = null;
        // рефлексийный вызов
        Method[] methods = last.getClass().getMethods();
        // поиск метода
        Method fun = null;
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                if (argsAmount == m.getParameterCount()) {
                    callArgs = toJvmArgs(vm, argsAmount);
                    fun = m;
                    break;
                }
            }
        }
        // выполнение метода
        if (fun == null) {
            throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "jvm method not found: " + last.getClass().getSimpleName() + ":" + name + " (args:" +
                    argsAmount + ")",
                    "check name for mistakes & passing args amount.");
        }
        else {
            checkArgs(last.getClass().getName() + ":" + name,
                    fun.getParameterCount()-1, callArgs.length-1);
            try {
                // 👇 ВОЗВРАЩАЕТ NULL, ЕСЛИ ФУНКЦИЯ НИЧЕГО НЕ ВОЗВРАЩАЕТ
                Object returned = fun.invoke(last, callArgs);
                if (shouldPushResult) {
                    vm.push(Objects.requireNonNullElseGet(returned, VmNull::new));
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new WattRuntimeError(
                        addr.getLine(), addr.getFileName(),
                        "reflection err: " + e.getMessage(), "check your code."
                );
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof WattRuntimeError ||
                        e.getCause() instanceof WattParsingError) {
                    throw e.getCause();
                } else {
                    throw new WattRuntimeError(
                            addr.getLine(), addr.getFileName(),
                            "reflection err: " + e.getCause().getMessage(), "check your code."
                    );
                }
            }
        }
    }

    // Преобразование в аргументы для jvm метода.
    private Object[] toJvmArgs(WattVM vm, int argsAmount) {
        // создаём список аргументов.
        Object[] callArgs = new Object[argsAmount];
        // заполняем его
        for (int i = argsAmount - 1; i >= 0; i--) {
            callArgs[i] = vm.pop(addr);
        }
        // возвращаем аргументы
        return callArgs;
    }

    // Вызов функции из глобального скоупа
    private void callGlobalFunc(WattVM vm, VmFrame<String, Object> frame)  {
        if (frame.has(name)) {
            // аргументы
            int argsAmount = passArgs(vm, frame);
            Object o = frame.lookup(addr, name);
            if (o instanceof VmFunction fn) {
                checkArgs(fn.getName(), fn.getArguments().size(), argsAmount);
                fn.exec(vm, shouldPushResult);
            }
            else if (o instanceof VmBuiltinFunction fn) {
                checkArgs(fn.getName(), fn.args(), argsAmount);
                fn.exec(vm, addr, shouldPushResult);
            } else {
                throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                        "couldn't call: " + o.getClass().getSimpleName(),
                        "check your code.");
            }
        } else {
            // аргументы
            int argsAmount = passArgs(vm, frame);
            // вызов
            Object o = vm.getGlobals().lookup(addr, name);
            if (o instanceof VmFunction fn) {
                checkArgs(fn.getName(), fn.getArguments().size(), argsAmount);
                fn.exec(vm, shouldPushResult);
            }
            else if (o instanceof VmBuiltinFunction fn) {
                checkArgs(fn.getName(), fn.args(), argsAmount);
                fn.exec(vm, addr, shouldPushResult);
            } else {
                throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                        "couldn't call: " + o.getClass().getSimpleName(),
                        "check your code.");
            }
        }
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(String name, int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "invalid args amount for call: "
                            + name + "(" + argsAmount + "/" + parameterAmount + ")",
                    "check passing args amount.");
        }
    }

    // помещает аргументы в стек
    private int passArgs(WattVM vm, VmFrame<String, Object> frame)  {
        int size = vm.getStack().size();
        for (VmInstruction instr : args.getInstructionContainer()) {
            instr.run(vm, frame);
        }
        return vm.getStack().size()-size;
    }

    @Override
    public String toString() {
        return "CALL_FUNCTION(" + name + ",instrs:" + args.getInstructionContainer().size() + ")";
    }
}
