package com.kilowatt.WattVM.Codegen;

import com.kilowatt.Errors.WattColors;
import com.kilowatt.WattVM.Instructions.VmInstruction;

import java.io.*;
import java.util.List;

/*
Дампер кода вм
 */
public class VmCodeDumper {
    // дамп
    public static void dump(List<VmInstruction> code, boolean toFile) {
        // выводим успешное сообщение о дампе
        System.out.println(WattColors.ANSI_YELLOW + "Dumping volt vm code...");
        // дамп
        // если в файл
        if (toFile) {
            // сохраняем стандартный вывод в консоль
            PrintStream originalOut = System.out;
            // файл
            File file = new File("out.vt");
            // перенаправляем вывод в файл
            try {
                System.setOut(new PrintStream(new FileOutputStream("out.vt")));
            } catch (FileNotFoundException e) {
                System.out.println("file not found: out.vt");
            }
            // пишем код в файл
            dumpCode(code);
            // возвращаем оригинал
            System.setOut(originalOut);
        } else {
            // пишем код в консоль
            dumpCode(code);
        }
        // выводим успешное сообщение о дампе
        System.out.println(WattColors.ANSI_YELLOW + "Dumped to out.vt");
    }

    // дамп кода
    public static void dumpCode(List<VmInstruction> code) {
        for (VmInstruction instruction : code) {
            instruction.print(0);
        }
    }

    // вывод
    public static void dumpLine(int indent, String text) {
        System.out.println("   ".repeat(indent) + text);
    }
}
