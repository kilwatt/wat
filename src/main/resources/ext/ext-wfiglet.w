unit wfiglet -> {
    // рефлекися
    figlet_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Ext.ExtFiglet',
        []
    )

    /*
     Функции
    */
    fun println(font_path, text) -> {
        return figlet_reflection.println(font_path, text)
    }
}