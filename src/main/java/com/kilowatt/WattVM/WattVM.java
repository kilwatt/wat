package com.kilowatt.WattVM;

import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.WattVM.Benchmark.VmBenchmark;
import com.kilowatt.WattVM.Codegen.WattVmCode;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Reflection.VmReflection;
import lombok.Getter;

import java.util.Stack;

/*
ВМ языка Watt
 */
@Getter
public class WattVM {
    // стэк
    private final ThreadLocal<Stack<Object>> stack = new ThreadLocal<>();
    private final VmFrame<String, Object> globals = new VmFrame<>();
    private final VmFrame<String, VmType> typeDefinitions = new VmFrame<>();
    private final VmFrame<String, VmUnit> unitDefinitions = new VmFrame<>();
    // рефлексия
    private final VmReflection reflection = new VmReflection(this);
    // адресс последнего вызова
    private final ThreadLocal<VmAddress> lastCallAddress = new ThreadLocal<>();

    // работа с адрессом последнего вызова
    public VmAddress getLastCallAddress() {
        return lastCallAddress.get();
    }

    public void setLastCallAddress(VmAddress address) {
        lastCallAddress.set(address);
    }

    // инициализация стека под поток
    public void initForThread() {
        stack.set(new Stack<>());
    }

    // получение стека
    public Stack<Object> getStack() {
        return stack.get();
    }

    // помещение в стек
    public void push(Object value) {
        getStack().push(value);
    }

    // удаление из стека
    public Object pop() {
        return getStack().pop();
    }

    // запуск
    public void run(WattVmCode code, boolean needBenchmarkInfo) {
        // code.print();
        // переменная для рефлексии
        globals.define(
            WattBuiltinProvider.builtinAddress,
        "reflection",
                reflection
        );
        // бенчмарк
        VmBenchmark mark = new VmBenchmark();
        mark.start();
        // инициализация под поток
        initForThread();
        // запуск кода
        code.run(this);
        // время выполнение
        if (needBenchmarkInfo) {
            System.out.println("exec time: " + mark.end() + " ms. stack: " + stack.get());
        }
    }
}
