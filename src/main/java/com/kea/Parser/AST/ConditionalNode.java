package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionCondOp;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Условие
 */
@AllArgsConstructor
public class ConditionalNode implements Node {
    private final Node left;
    private final Node right;
    private final Token operator;

    @Override
    public void compile() {
        left.compile();
        right.compile();
        KeaCompiler.code.visitInstruction(
                new VmInstructionCondOp(
                        new VmAddress(operator.getFileName(), operator.getLine()),
                        operator.value
                )
        );
    }
}
