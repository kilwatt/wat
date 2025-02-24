package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.Instructions.VmInstructionIf;
import com.kea.KeaVM.Instructions.VmInstructionLoop;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import com.kea.Lexer.TokenType;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
While
 */
@AllArgsConstructor
public class WhileNode implements Node {
    private final Token location;
    private final BlockNode node;
    private final Node logical;

    @Override
    public void compile() {
        VmInstructionLoop loop = new VmInstructionLoop(new VmAddress(location.fileName, location.line));
        KeaCompiler.code.writeTo(loop.getInstructions());
        compileLogical();
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(loop);
    }

    private void compileLogical() {
        new IfNode(
                location, node, logical,
                new IfNode(
                        location,
                        BlockNode.of(new BreakNode(location)),
                        new BoolNode(
                                new Token(TokenType.BOOL, "true",
                                        location.getLine(), location.getFileName()
                                )),
                        null
                )
        ).compile();
    }
}
