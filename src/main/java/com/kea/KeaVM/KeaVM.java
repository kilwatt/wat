package com.kea.KeaVM;

import com.kea.Compiler.KeaBuiltinProvider;
import com.kea.KeaVM.Benchmark.VmBenchmark;
import com.kea.KeaVM.Codegen.KeaVmCode;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.Reflection.VmReflection;
import lombok.Getter;

import java.util.Stack;

/*
ВМ языка Кеа
 */
@Getter
public class KeaVM {
    // стэк
    private final ThreadLocal<Stack<Object>> stack = new ThreadLocal<>();
    private final VmFrame<String, Object> globals = new VmFrame<>();
    private final Stack<VmFrame<String, Object>> callStack = new Stack<>();
    private final VmFrame<String, VmType> typeDefinitions = new VmFrame<String, VmType>();
    private final VmFrame<String, VmUnit> unitDefinitions = new VmFrame<String, VmUnit>();

    // инициализация стека под поток
    public void initForThread() {
        stack.set(new Stack<>());
    }

    // получение стека
    public Stack<Object> getStack() {
        return stack.get();
    }

    // помещение фрейма в стек вызовов
    public void pushCallFrame(VmFrame<String, Object> callFrame) {
        callStack.push(callFrame);
    }

    // удаление фрейма из стека вызовов
    public void popCalFrame() {
        callStack.pop();
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
    public void run(KeaVmCode code) {
        // code.print();
        // переменная для рефлексии
        globals.define(
            KeaBuiltinProvider.builtinAddress,
        "reflection",
                new VmReflection(this)
        );
        // бенчмарк
        VmBenchmark mark = new VmBenchmark();
        mark.start();
        // инициализация под поток
        initForThread();
        // запуск кода
        code.run(this);
        // время выполнение
        System.out.println("Exec time: " + mark.end() + " ms. Stack: " + stack.get());
    }
}
