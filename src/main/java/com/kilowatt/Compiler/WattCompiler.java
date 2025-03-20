package com.kilowatt.Compiler;

import com.kilowatt.WattVM.Codegen.WattVmCode;
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

    // импорт дефайнов
    public static void importDefinitions(BlockNode parse) {
        for (Node node : parse.getNodes()) {
            if (node instanceof UnitNode ||
                node instanceof TypeNode ||
                node instanceof FnNode ||
                node instanceof ImportNode) {
                node.compile();
            }
        }
    }
}
