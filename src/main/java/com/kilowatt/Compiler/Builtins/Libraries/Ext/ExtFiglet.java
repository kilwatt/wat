package com.kilowatt.Compiler.Builtins.Libraries.Ext;


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
public class ExtFiglet {
    public void println(String fontName, String text) {
        // генерация арта
        String asciiArt;
        try {
            Path path = Path.of(fontName);
            asciiArt = FigletFont.convertOneLine(new File(path.toUri()), text);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "io exception in figlet: " + e.getMessage(),
                "check your code."
            );
        }
        // вывод
        System.out.println(asciiArt);
    }
}
