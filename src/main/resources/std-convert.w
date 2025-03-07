unit convert -> {
    std_reflection_converter := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdConverter',
        []
    )
    std_reflection_strings := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdStrings',
        []
    )

    fun to_int(value) -> {
        return std_reflection_converter.to_int(value)
    }

    fun to_float(value) -> {
        return std_reflection_converter.to_float(value)
    }

    fun to_long(value) -> {
        return std_reflection_converter.to_long(value)
    }

    fun to_string(value) -> {
        if value == null {
            return 'null'
        }

        return std_reflection_converter.to_string(value)
    }

    fun parse_int(value) -> {
        return std_reflection_strings.parse_int(value)
    }

    fun parse_float(value) -> {
        return std_reflection_strings.parse_float(value)
    }

    fun parse_long(value) -> {
        return std_reflection_strings.parse_long(value)
    }
}