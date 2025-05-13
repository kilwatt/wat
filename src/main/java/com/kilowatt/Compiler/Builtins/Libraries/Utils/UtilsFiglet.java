package com.kilowatt.Compiler.Builtins.Libraries.Utils;


import com.github.lalyos.jfiglet.FigletFont;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/*
Ext -> Figlet
 */
public class UtilsFiglet {
    public String art(String fontName, String text) {
        // генерация арта
        String asciiArt;
        try {
            Path path = Path.of(fontName);
            asciiArt = FigletFont.convertOneLine(new File(path.toUri()), text);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "io exception in figlet: " + e.getMessage(),
                "check your code."
            );
        }
        // возвращаем
        return asciiArt;
    }

    public void println(String fontName, String text) {
        // вывод арта
        System.out.println(art(fontName, text));
    }
}
