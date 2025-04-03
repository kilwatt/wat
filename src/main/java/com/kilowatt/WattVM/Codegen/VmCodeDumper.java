package com.kilowatt.WattVM.Codegen;

import com.kilowatt.WattVM.Instructions.VmInstruction;

import java.util.List;

/*
Дампер кода вм
 */
public class VmCodeDumper {
    // дамп кода
    public static void dump(List<VmInstruction> code) {
        System.out.println("******** Volt Vm Code Dump ********");
        for (VmInstruction instruction : code) {
            instruction.print(0);
        }
        System.out.println("***********************************");
    }

    // вывод
    public static void dumpLine(int indent, String text) {
        System.out.println("   ".repeat(indent) + text);
    }
}
