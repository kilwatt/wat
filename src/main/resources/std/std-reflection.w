import 'std.io'

unit reflect -> {
    // рефлекися
    refl_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdReflection',
        []
    )

    /*
    Функции
    */
    fun lookup_type(name) -> {
        return refl_reflection.lookup_type(name)
    }

    fun lookup_unit(name) -> {
        return refl_reflection.lookup_unit(name)
    }

    fun instance(_type, args) -> {
        return refl_reflection.instance(_type, args)
    }

    fun call_function(instance, name, args) -> {
        return refl_reflection.call_function(instance, name, args)
    }

    fun get_field(instance, name) -> {
        return refl_reflection.get_field(instance, name)
    }
}