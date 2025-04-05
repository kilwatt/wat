package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Instructions.VmInstructionNeg;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Унарная операция
 */
@SuppressWarnings("SwitchStatementWithTooFewBranches")
@Getter
@AllArgsConstructor
public class UnaryNode implements Node {
    private final Token op;
    private final Node node;

    @Override
    public void compile() {
        switch (op.value) {
            case "-" -> {
                node.compile();
                WattCompiler.code.visitInstruction(
                    new VmInstructionNeg(
                        op.asAddress()
                    )
                );
            }
            default -> throw new WattRuntimeError(
                op.getLine(),
                op.getFileName(),
                "invalid unary op: " + op.value,
                "available op-s: -"
            );
        }
    }
}
