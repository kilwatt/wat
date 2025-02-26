package com.kea.Compiler;

import com.kea.KeaVM.Codegen.KeaVmCode;
import com.kea.KeaVM.KeaVM;
import com.kea.Parser.AST.*;
import lombok.Getter;

/*
Компилятор
 */
@Getter
public class KeaCompiler {
    public static KeaVmCode code = new KeaVmCode();
    public static final KeaVM vm = new KeaVM();
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
