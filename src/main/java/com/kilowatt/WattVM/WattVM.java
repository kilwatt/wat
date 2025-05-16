package com.kilowatt.WattVM;

import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Benchmark.VmBenchmark;
import com.kilowatt.WattVM.Codegen.WattVmCode;
import com.kilowatt.WattVM.Entities.VmNull;
import com.kilowatt.WattVM.Entities.VmTrait;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Reflection.VmCallInfo;
import com.kilowatt.WattVM.Reflection.VmReflection;
import com.kilowatt.WattVM.Entities.VmTable;
import com.kilowatt.WattVM.Threads.VmThreads;
import com.kilowatt.WattVM.Trace.VmCallsTrace;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/*
ВМ языка Watt
 */
@Getter
public class WattVM {
    // стэк
    private final ThreadLocal<ArrayDeque<Object>> stack = new ThreadLocal<>();
    // глобальные переменные
    private final VmTable<String, Object> globals = new VmTable<>();
    private final VmTable<String, VmType> typeDefinitions = new VmTable<>();
    private final VmTable<String, VmUnit> unitDefinitions = new VmTable<>();
    private final VmTable<String, VmTrait> traitDefinitions = new VmTable<>();
    // рефлексия
    private final VmReflection reflection = new VmReflection(this);
    // трэйс(история) вызовов
    private final ThreadLocal<VmCallsTrace> callsTrace = new ThreadLocal<>();
    // потоки
    private final VmThreads threads = new VmThreads();

    // получение провайдера истории вызовов
    public VmCallsTrace getCallsTrace() {
        return callsTrace.get();
    }

    // получение истории вызовов
    public List<VmCallInfo> getCallsHistory() {
        return callsTrace.get().getCallsHistory();
    }

    // инициализация вм под поток
    public void initForThread() {
        // устанавливаем стэк
        stack.set(new ArrayDeque<>());
        // устанавливаем историю вызовов
        callsTrace.set(new VmCallsTrace());
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
    public Object pop(VmAddress address) {
        try {
            return getStack().pop();
        } catch (NoSuchElementException e) {
            throw new WattRuntimeError(
                address,
                "couldn't pop. stack is empty.",
                "check your actions."
            );
        }
    }

    // запуск
    public void run(WattVmCode code, boolean needBenchmarkInfo) {
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
        // ждём завершения всех потоков
        threads.awaitAll();
        // время выполнения
        if (needBenchmarkInfo) {
            System.out.println("exec time: " + mark.end() + " ms. stack: " + stack.get());
        }
    }

    public void dump(WattVmCode code, boolean asFile) {
        // дамп кода
        code.print(asFile);
    }
}
