unit threads -> {
    // рефлекися
    threads_reflection := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdThreading',
        []
    )

    /*
     Функции
    */
    fun sleep(time) -> {
        threads_reflection.sleep(time)
    }

    fun run(fn, constructor) -> {
        threads_reflection.run(fn, constructor)
    }
}