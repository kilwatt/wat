unit jvm {
    // рефлекися
    jvm_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdJvm',
        []
    )

    /*
     Функции
    */
    fun array_length(arr) {
        return jvm_reflection.array_length(arr)
    }

    fun array_element(arr, index) {
        return jvm_reflection.array_element(arr, index)
    }

    fun reflect(name, constructor) {
        return reflection.reflect(name, constructor)
    }
}