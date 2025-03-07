package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.*;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
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
        WattCompiler.code.writeTo(loop.getInstructions());
        compileIncrement(range.isDecrement());
        compileLogical(range.isDecrement());
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(loop);
    }

    private void compileDefinition(boolean isDecrement) {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(box);
        if (!isDecrement) {
            range.getFrom().compile();
            WattCompiler.code.visitInstruction(new VmInstructionPush(name.asAddress(), 1));
            WattCompiler.code.visitInstruction(new VmInstructionBinOp(name.asAddress(), "-"));
        } else {
            range.getTo().compile();
            WattCompiler.code.visitInstruction(new VmInstructionPush(name.asAddress(), 1));
            WattCompiler.code.visitInstruction(new VmInstructionBinOp(name.asAddress(), "+"));
        }
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(
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
        WattCompiler.code.writeTo(box);
        WattCompiler.code.visitInstruction(new VmInstructionLoad(name.asAddress(), name.value, false, true));
        WattCompiler.code.visitInstruction(new VmInstructionPush(name.asAddress(), 1));
        WattCompiler.code.visitInstruction(new VmInstructionBinOp(name.asAddress(), isDecrement ? "-" : "+"));
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(
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
}
