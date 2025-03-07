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
    public static WattVmCode code = new WattVmCode();
    public static final WattVM vm = new WattVM();
    public static void compile(Node parse) {
        parse.compile();
    }
    public static void importDefinitions(BlockNode parse) {
        for (Node node : parse.getNodes()) {
            if (node instanceof UnitNode ||
                node instanceof TypeNode ||
                node instanceof FnNode) {
                node.compile();
            }
        }
    }
}
