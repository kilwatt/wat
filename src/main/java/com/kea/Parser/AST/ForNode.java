package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.*;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import com.kea.Lexer.TokenType;
import lombok.AllArgsConstructor;

/*
Цикл for
 */
@AllArgsConstructor
public class ForNode implements Node {
    private final BlockNode body;
    private final Token name;
    private final RangeNode range;

    @Override
    public void compile() {
        compileDefinition(range.isDecrement());
        VmInstructionLoop loop = new VmInstructionLoop(new VmAddress(name.fileName, name.line));
        KeaCompiler.code.writeTo(loop.getInstructions());
        compileLogical(range.isDecrement());
        compileIncrement(range.isDecrement());
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(loop);
    }

    private void compileDefinition(boolean isDecrement) {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        if (!isDecrement) {
            range.getFrom().compile();
        } else {
            range.getTo().compile();
        }
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        name.asAddress(),
                        name.value,
                        false,
                        box
                )
        );
    }

    private void compileIncrement(boolean isDecrement) {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        KeaCompiler.code.visitInstruction(new VmInstructionLoad(name.asAddress(), name.value, false, true));
        KeaCompiler.code.visitInstruction(new VmInstructionPush(name.asAddress(), 1));
        KeaCompiler.code.visitInstruction(new VmInstructionBinOp(name.asAddress(), isDecrement ? "-" : "+"));
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(
                new VmInstructionSet(
                        name.asAddress(),
                        name.value,
                        false,
                        box
                )
        );
    }

    private void compileLogical(boolean isDecrement) {
        new IfNode(
                name,
                body,
                new ConditionalNode(
                        new VarNode(null, name, true),
                        isDecrement ? range.getFrom() : range.getTo(),
                        new Token(
                                isDecrement ? TokenType.BIGGER : TokenType.LOWER,
                                isDecrement ? ">" : "<",
                                name.line, name.getFileName()
                        )
                ),
                new IfNode(
                        name,
                        BlockNode.of(new BreakNode(name)),
                        new BoolNode(
                                new Token(TokenType.BOOL,
                                        "true",
                                        name.getLine(),
                                        name.getFileName())
                        ),
                        null
                )
        ).compile();
    }

    private void compileLogicalDecrement() {
        new IfNode(
                name,
                body,
                new ConditionalNode(
                        new VarNode(null, name, true),
                        range.getFrom(),
                        new Token(
                                TokenType.LOWER,">",
                                name.line, name.getFileName()
                        )
                ),
                new IfNode(
                        name,
                        BlockNode.of(new BreakNode(name)),
                        new BoolNode(
                                new Token(TokenType.BOOL,
                                        "true",
                                        name.getLine(),
                                        name.getFileName())
                        ),
                        null
                )
        ).compile();
    }
}
