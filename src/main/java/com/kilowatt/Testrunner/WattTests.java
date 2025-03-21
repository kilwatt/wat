package com.kilowatt.Testrunner;

/*
Тесты
 */
public class WattTests {
    public static void run() {
        WattTestRunner runner = new WattTestRunner(
                WattTest.builder().path(
                                "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\main\\logical.w")
                        .name("logical")
                        .build(),
                WattTest.builder().path(
                                "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\main\\match.w")
                        .name("pattern matching")
                        .build(),
                WattTest.builder().path(
                                "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\fp\\anonymous_fn.w").
                        name("anonymous fn")
                        .build(),
                WattTest.builder().path(
                                "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\fp\\lambdas.w").
                        name("lambdas")
                        .build(),
                WattTest.builder().path(
                                "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\fp\\pipes.w").
                        name("pipes")
                        .build(),
                WattTest.builder().path(
                            "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\reflection\\reflection.w").
                        name("jvm reflection")
                        .build(),
                WattTest.builder().path(
                            "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\std\\math\\std_math_base_test.w").
                        name("std math base")
                        .build(),
                WattTest.builder().path(
                                "E:\\wat_lang\\wat\\src\\test\\watt\\tests\\std\\math\\std_math_ext_test.w").
                        name("std ext base")
                        .build()
        );
        runner.run();
    }
}
