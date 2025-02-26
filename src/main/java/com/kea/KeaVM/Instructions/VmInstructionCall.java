package com.kea.KeaVM.Instructions;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Builtins.VmBuiltinFunction;
import com.kea.KeaVM.Entities.VmFunction;
import com.kea.KeaVM.Entities.VmInstance;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        // вызов
        if (!hasPrevious) {
            callGlobalFunc(vm, frame);
        } else {
            Object last = vm.pop();
            if (last instanceof VmInstance vmInstance) {
                callInstanceFunc(vm, frame, vmInstance);
            } else if (last instanceof VmUnit vmUnit){
                callUnitFunc(vm, frame, vmUnit);
            } else {
                callReflectionFunc(vm, frame, last);
            }
        }
    }

    // Вызывает функцю объекта
    private void callInstanceFunc(KeaVM vm, VmFrame<String, Object> frame, VmInstance vmObj)  {
        // аргументы и поиск функции
        int argsAmount = passArgs(vm, frame);
        VmFunction fn = (VmFunction) vmObj.getScope().lookup(addr, name);
        checkArgs(vmObj.getType().getName() + "->" + name, fn.getArguments().size(), argsAmount);
        // вызов
        vmObj.call(addr, name, vm, shouldPushResult);
    }

    // Вызывает функцю юнита
    private void callUnitFunc(KeaVM vm, VmFrame<String, Object> frame, VmUnit unit)  {
        // аргументы и поиск функции
        int argsAmount = passArgs(vm, frame);
        VmFunction fn = (VmFunction) unit.getFields().lookup(addr, name);
        checkArgs(unit.getName() + "->" + name, fn.getArguments().size(), argsAmount);
        // вызов
        fn.exec(vm, shouldPushResult);
    }

    // Вызывает рефлексийную функцию
    @SneakyThrows
    private void callReflectionFunc(KeaVM vm, VmFrame<String, Object> frame, Object last)  {
        // аргументы
        int argsAmount = passArgs(vm, frame);
        ArrayList<Object> callArgs = new ArrayList<>();
        for (int i = argsAmount-1; i >= 0; i--) {
            Object arg = vm.pop();
            callArgs.addFirst(arg);
        }
        callArgs.addFirst(addr);
        // рефлексийный вызов
        Method[] methods = last.getClass().getMethods();
        Method func = null;
        for (Method m : methods) {
            if (m.getName().equals(name) &&
                    m.getParameterCount() == callArgs.size()) {
                func = m;
            }
        }
        if (func == null) {
            throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Jvm func not found: " + last.getClass().getName() + "->" + name,
                    "Check name for mistakes and args amount!");
        }
        else {
            checkArgs(last.getClass().getName() + "->" + name,
                    func.getParameterCount()-1, callArgs.size()-1);
            try {
                // 👇 ВОЗВРАЩАЕТ NULL, ЕСЛИ ФУНКЦИЯ НИЧЕГО НЕ ВОЗВРАЩАЕТ
                Object returned = func.invoke(last, callArgs.toArray());
                if (shouldPushResult) {
                    vm.push(returned);
                }
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new KeaRuntimeError(
                        addr.getLine(), addr.getFileName(),
                        "Reflection error: " + e, "Check your code!"
                );
            }
        }
    }

    // Вызов функции из глобального скоупа
    private void callGlobalFunc(KeaVM vm, VmFrame<String, Object> frame)  {
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
                fn.exec(vm, addr);
            } else {
                throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                        "Can't call: " + o.getClass().getSimpleName(),
                        "Check your code!");
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
                fn.exec(vm, addr);
            } else {
                throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                        "Can't call: " + o.getClass().getSimpleName(),
                        "Check your code!");
            }
        }
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(String name, int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Invalid args amount for call of func: "
                            + name + "(" + argsAmount + "/" + parameterAmount + ")",
                    "Check arguments amount!");
        }
    }

    // помещает аргументы в стек
    private int passArgs(KeaVM vm, VmFrame<String, Object> frame)  {
        int size = vm.getStack().size();
        for (VmInstruction instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
        return vm.getStack().size()-size;
    }

    @Override
    public String toString() {
        return "CALL_FUNCTION(" + name + ",instrs:" + args.getVarContainer().size() + ")";
    }
}
