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
        compileDefinition();
        VmInstructionLoop loop = new VmInstructionLoop(new VmAddress(name.fileName, name.line));
        KeaCompiler.code.writeTo(loop.getInstructions());
        compileLogical(range.isDecrement());
        compileIncrement(range.isDecrement());
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(loop);
    }

    private void compileDefinition() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        range.getFrom().compile();
        KeaCompiler.code.visitInstruction(new VmInstructionPush(name.asAddress(), 1));
        KeaCompiler.code.visitInstruction(new VmInstructionBinOp(name.asAddress(), "-"));
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
        Node first = isDecrement ? range.getTo() : new VarNode(null, name, true);
        Node second = isDecrement ? new VarNode(null, name, true) : range.getTo();
        new IfNode(
                name,
                body,
                new ConditionalNode(
                        first,
                        second,
                        new Token(
                                TokenType.LOWER, "<",
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
