package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParseError;
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
            default -> throw new WattParseError(
                op.asAddress(),
                "invalid unary op: " + op.value,
                "available op-s: -, !"
            );
        }
    }
}
