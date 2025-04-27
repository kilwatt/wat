package com.kilowatt.WattVM.Threads;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.*;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/*
Потоки ВМ.
 */
@SuppressWarnings("UnnecessaryContinue")
@Getter
public class VmThreads {
    // счётчик потоков
    private final AtomicInteger runningThreads = new AtomicInteger(0);

    // ожидание завершения
    public void awaitAll() {
        while (runningThreads.get() > 0) {
            continue;
        }
    }

    // субмит потока
    public Thread submit(Runnable task) {
        // увеличиваем счётчик потоков
        runningThreads.incrementAndGet();
        // создаём поток
        Thread thread = new Thread(() -> {
            try {
                WattCompiler.vm.initForThread();
                task.run();
            } catch (WattError error) {
                error.panic();
            } catch (RuntimeException error) {
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                new WattRuntimeError(
                        address.getLine(),
                        address.getFileName(),
                        "jvm runtime exception: " + error.getMessage(),
                        "check your code."
                ).panic();
            }
            finally {
                runningThreads.decrementAndGet();
            }
        });
        thread.start();
        return thread;
    }

    // новый поток
    public Thread newThread(Runnable task) {
        // увеличиваем счётчик потоков
        runningThreads.incrementAndGet();
        // создаём поток
        return new Thread(() -> {
            try {
                WattCompiler.vm.initForThread();
                task.run();
            } catch (WattError error) {
                error.panic();
            } catch (RuntimeException error) {
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                new WattRuntimeError(
                        address.getLine(),
                        address.getFileName(),
                        "jvm runtime exception: " + error.getMessage(),
                        "check your code."
                ).panic();
            }
            finally {
                runningThreads.decrementAndGet();
            }
        });
    }
}
