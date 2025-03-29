unit random -> {
    // рефлекися
    random_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.Math.StdRandom',
        []
    )

    /*
     Функции с числами
    */

    fun int(origin, bound) -> {
        return random_reflection.int_random(origin, bound)
    }

    fun float(origin, bound) -> {
        return random_reflection.float_random(origin, bound)
    }

    fun long(origin, bound) -> {
        return random_reflection.long_random(origin, bound)
    }

    fun choice(arr) -> {
        index := self.int(0, arr.size())
        return arr.get(index)
    }
}