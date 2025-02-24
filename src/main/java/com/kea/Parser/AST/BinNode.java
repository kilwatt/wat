package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionBinOp;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Бинарная операция
 */
@Getter
@AllArgsConstructor
public class BinNode implements Node {
    private final Node left;
    private final Node right;
    private final Token operator;

    @Override
    public void compile() {
        left.compile();
        right.compile();
        KeaCompiler.code.visitInstruction(new VmInstructionBinOp(
                new VmAddress(operator.fileName, operator.line), operator.value
        ));
    }
}
