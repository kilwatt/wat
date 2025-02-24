package com.kea.KeaVM;

import com.kea.KeaVM.Benchmark.VmBenchmark;
import com.kea.KeaVM.Codegen.KeaVmCode;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
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
        VmBenchmark mark = new VmBenchmark();
        mark.start();
        initForThread();
        code.run(this);
        System.out.println("Exec time: " + mark.end() + " ms ");
    }
}
