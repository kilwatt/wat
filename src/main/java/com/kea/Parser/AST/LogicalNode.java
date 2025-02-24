package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionLogicalOp;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

/*
Логическая нода
 */
@AllArgsConstructor
public class LogicalNode implements Node {
    private final Node left;
    private final Node right;
    private final Token operator;

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(new VmInstructionLogicalOp(operator.asAddress(), operator.value));
    }
}
