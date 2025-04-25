package com.kilowatt.WattVM.Threads;

import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/*
Кастомный тред пул для правильной
работы с потоками в ВМ
 */
@AllArgsConstructor
@Getter
public class VmThreadPool extends QueuedThreadPool {
    // вм
    private final WattVM vm;

    // новый поток
    @Override
    public Thread newThread(Runnable task) {
        return vm.getThreads().newThread(task);
    }
}
