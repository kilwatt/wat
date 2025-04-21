package com.kilowatt.Compiler;

import com.kilowatt.WattVM.Codegen.WattVmCode;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.Parser.AST.*;
import lombok.Getter;

/*
Компилятор
 */
@Getter
public class WattCompiler {
    // код вм
    public static WattVmCode code = new WattVmCode();
    // вм
    public static WattVM vm = new WattVM();

    // ресет компилятора и вм
    public static void reset() {
        code = new WattVmCode();
        vm = new WattVM();
    }

    // компиляция
    public static void compile(Node parse) {
        // ресетаем
        reset();
        // компилируем
        parse.compile();
    }

    // импорт дефайнов и их компиляция
    public static void compileImport(VmAddress address, BlockNode block) {
        // компилируем
        for (Node node : block.getNodes()) {
            if (node instanceof UnitNode ||
                node instanceof TypeNode ||
                node instanceof FnNode ||
                node instanceof NativeNode ||
                node instanceof ImportNode) {
                node.compile();
            }
        }
    }
}
