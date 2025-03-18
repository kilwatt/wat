package com.kilowatt.Compiler.Builtins.Libraries.Std.Math;

import lombok.Getter;

import java.util.Random;

/*
Стд -> рандом
 */
@Getter
public class StdRandom {
    // рандом
    private final Random random = new Random();

    /*
    рандомизация чисел
     */
    public int int_random(int origin, int bound) {
        return random.nextInt(origin, bound);
    }

    public float float_random(float origin, float bound) {
        return random.nextFloat(origin, bound);
    }

    public long long_random(long origin, long bound) {
        return random.nextLong(origin, bound);
    }
}
