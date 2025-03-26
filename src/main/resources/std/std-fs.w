unit fs -> {
    std_reflection_fs := reflection.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.StdFs',
        []
    )

    fun open_reader(path) -> {
        return std_reflection_fs.open_reader(path)
    }

    fun open_writer(path) -> {
        return std_reflection_fs.open_writer(path)
    }

    fun is_exists(path) -> {
        return std_reflection_fs.is_exists(path)
    }

    fun create_file(path) -> {
        std_reflection_fs.create_file(path)
    }

    fun create_directory(path) -> {
        std_reflection_fs.create_directory(path)
    }

    fun delete_path(path) -> {
        std_reflection_fs.delete_path(path)
    }
}