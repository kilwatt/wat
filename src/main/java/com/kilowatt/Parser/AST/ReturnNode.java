package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionReturn;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.writeTo(box);
        forReturn.compile();
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(
                new VmInstructionReturn(
                        box,
                        location.asAddress()
                )
        );
    }
}
