package com.kilowatt.Testrunner;

/*
Тесты
 */
public class WattTests {
    public static void run() {
        WattTestRunner runner = new WattTestRunner(
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\main\\logical.wt")
                        .name("logical")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\main\\match.wt")
                        .name("pattern matching")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\main\\types.wt")
                        .name("custom types")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\main\\units.wt")
                        .name("custom units")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\main\\while.wt")
                        .name("while")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\main\\for.wt")
                        .name("for")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\fp\\anonymous_fn.wt").
                        name("anonymous fn")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\fp\\lambdas.wt").
                        name("lambdas")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\fp\\pipes.wt").
                        name("pipes")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\reflection\\reflection.wt").
                        name("jvm reflection")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\reflection\\wattreflection.wt").
                        name("watt reflection")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\std\\math\\std_math_base_test.wt").
                        name("std math base")
                        .build(),
                WattTest.builder().path(
                                "G:\\Kilowatt\\Watt\\src\\test\\watt\\tests\\std\\math\\std_math_ext_test.wt").
                        name("std ext base")
                        .build()
        );
        runner.run();
    }
}
