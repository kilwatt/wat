unit math -> {
    // константы
    pi := 3.141592
    e := 2.718281
    tau := 6.283185

    // рефлекися
    math_reflection := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.Math.StdMath',
        []
    )

    // функции
    fun ceil(x) -> {
        return math_reflection.ceil(x)
    }

    fun abs(x) -> {
        return math_reflection.abs(x)
    }

    fun floor(x) -> {
        return math_reflection.floor(x)
    }

    fun sqrt(x) -> {
        return math_reflection.sqrt(x)
    }

    fun cbrt(x) -> {
        return math_reflection.cbrt(x)
    }

    fun pow(x, y) -> {
        return math_reflection.pow(x, y)
    }

    fun log(x) -> {
        return math_reflection.log(x)
    }

    fun log10(x) -> {
        return math_reflection.log10(x)
    }
}