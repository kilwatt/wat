unit math_ext -> {
    // константы
    pi := 3.141592
    e := 2.718281
    tau := 6.283185

    // рефлекися
    math_reflection := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.Math.StdMathExt',
        []
    )

    // функции
    fun sin(x) -> {
        return math_reflection.sin(x)
    }

    fun cos(x) -> {
        return math_reflection.cos(x)
    }

    fun tan(x) -> {
        return math_reflection.tan(x)
    }

    fun asin(x) -> {
        return math_reflection.asin(x)
    }

    fun acos(x) -> {
        return math_reflection.acos(x)
    }

    fun atan(x) -> {
        return math_reflection.atan(x)
    }
}