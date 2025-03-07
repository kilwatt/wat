unit convert -> {
    std_reflection_converter := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdConverter',
        []
    )
    std_reflection_strings := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdStrings',
        []
    )

    func to_int(value) -> {
        return std_reflection_converter.to_int(value)
    }

    func to_float(value) -> {
        return std_reflection_converter.to_float(value)
    }

    func to_long(value) -> {
        return std_reflection_converter.to_long(value)
    }

    func to_string(value) -> {
        if value == null {
            return 'null'
        }

        return value.toString()
    }

    func parse_int(value) -> {
        return std_reflection_strings.parse_int(value)
    }

    func parse_float(value) -> {
        return std_reflection_strings.parse_float(value)
    }

    func parse_long(value) -> {
        return std_reflection_strings.parse_long(value)
    }
}