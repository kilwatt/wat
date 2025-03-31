package com.kilowatt.WattVM;

import com.kilowatt.WattVM.Instructions.VmInstruction;

import java.util.List;

/*
Дампер кода вм
 */
public class VmCodeDumper {
    public static void dump(List<VmInstruction> code) {
        System.out.println("*********** VmCode Dump ***********");
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
