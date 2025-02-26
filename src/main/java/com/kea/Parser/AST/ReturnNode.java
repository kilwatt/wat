package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionLoad;
import com.kea.KeaVM.Instructions.VmInstructionReturn;
import com.kea.Lexer.Token;
import com.kea.Lexer.TokenType;
import lombok.AllArgsConstructor;

/*
Return
 */
@AllArgsConstructor
public class ReturnNode implements Node {
    private final Token location;
    private final Node forReturn;

    @Override
    public void compile() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        forReturn.compile();
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(
                new VmInstructionReturn(
                        box,
                        location.asAddress()
                )
        );
    }
}
