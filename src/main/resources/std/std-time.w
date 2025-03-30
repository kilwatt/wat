unit time {
    // рефлекися
    time_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdTime',
        []
    )

    /*
    функции
    */
    fun now() {
        return time_reflection.now()
    }

    fun date() {
        return time_reflection.date()
    }

    /*
    форматирование
    */
    fun format(time) {
        return time_reflection.format(time)
    }
}