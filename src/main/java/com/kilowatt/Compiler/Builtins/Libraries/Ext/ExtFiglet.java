package com.kilowatt.Compiler.Builtins.Libraries.Ext;


import com.github.lalyos.jfiglet.FigletFont;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.io.File;
import java.io.IOException;

/*
Ext -> Figlet
 */
public class ExtFiglet {
    public void println(String fontName, String text) {
        // генерация арта
        String asciiArt = null;
        try {
            asciiArt = FigletFont.convertOneLine(new File(fontName), text);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
