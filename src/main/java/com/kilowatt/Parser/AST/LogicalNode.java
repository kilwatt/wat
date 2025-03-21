package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionLogicalOp;
import com.kilowatt.Lexer.Token;
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
        left.compile();
        right.compile();
        WattCompiler.code.visitInstruction(new VmInstructionLogicalOp(operator.asAddress(), operator.value));
    }
}
