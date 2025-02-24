package com.kea.Compiler;

import com.kea.KeaVM.Codegen.KeaVmCode;
import com.kea.KeaVM.KeaVM;
import com.kea.Lexer.Token;
import com.kea.Lexer.TokenType;
import com.kea.Parser.AST.BoolNode;
import com.kea.Parser.AST.IfNode;
import com.kea.Parser.AST.Node;
import lombok.Getter;

/*
Компилятор
 */
@Getter
public class KeaCompiler {
    public static final KeaVmCode code = new KeaVmCode();
    public static final KeaVM vm = new KeaVM();
    public static void compile(Node parse) {
        parse.compile();
    }
}
