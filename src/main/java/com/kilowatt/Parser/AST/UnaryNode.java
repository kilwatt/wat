package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import com.kilowatt.WattVM.Instructions.VmInstructionNeg;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Унарная операция
 */
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
            case "!" -> {
                node.compile();
                WattCompiler.code.visitInstruction(new VmInstructionPush(
                   op.asAddress(),
                   false
                ));
                WattCompiler.code.visitInstruction(
                    new VmInstructionCondOp(
                            op.asAddress(),
                            "=="
                    )
                );
            }
            default -> throw new WattRuntimeError(
                op.getLine(),
                op.getFileName(),
                "invalid unary op: " + op.value,
                "available op-s: -, !"
            );
        }
    }
}
