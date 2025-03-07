package com.kilowatt.WattVM.Benchmark;

/*
Бенчмарки VM
 */
public class VmBenchmark {
    // первое зафиксированное время в мс
    private long startTime;

    /**
     * Запускает таймер
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Останавливает таймер и возвращает
     * секунды выполнения.
     * @return startTime - возвращает
     * время выполнения в секундах.
     */
    public double end() {
        return ((Long)(System.nanoTime()-startTime)).doubleValue()/1000000.0;
    }
}
