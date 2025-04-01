package com.kilowatt.WattVM;

import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.WattVM.Benchmark.VmBenchmark;
import com.kilowatt.WattVM.Codegen.WattVmCode;
import com.kilowatt.WattVM.Entities.VmNull;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Reflection.VmReflection;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Objects;

/*
ВМ языка Watt
 */
@Getter
public class WattVM {
    // стэк
    private final ThreadLocal<ArrayDeque<Object>> stack = new ThreadLocal<>();
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
        stack.set(new ArrayDeque<>());
    }

    // получение стека
    public ArrayDeque<Object> getStack() {
        return stack.get();
    }

    // помещение в стек
    public void push(Object value) {
        getStack().push(Objects.requireNonNullElseGet(value, VmNull::new));
    }

    // удаление из стека
    public Object pop() {
        return getStack().pop();
    }

    // запуск
    public void run(WattVmCode code, boolean needBenchmarkInfo) {
        code.print();
        // переменная для рефлексии
        globals.define(
            WattBuiltinProvider.builtinAddress,
        "__refl__",
                reflection
        );
        // инициализация под поток
        initForThread();
        // бенчмарк
        VmBenchmark mark = new VmBenchmark();
        mark.start();
        // запуск кода
        code.run(this);
        // время выполнение
        if (needBenchmarkInfo) {
            System.out.println("exec time: " + mark.end() + " ms. stack: " + stack.get());
        }
    }
}
