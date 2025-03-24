package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionTry;
import lombok.AllArgsConstructor;

/*
try-нода
 */
@AllArgsConstructor
public class TryNode implements Node {
    private final BlockNode tryNode;
    private final BlockNode catchNode;
    private final Token catchVariableName;

    @Override
    public void compile() {
        // бокс для try
        VmBaseInstructionsBox tryBox = new VmBaseInstructionsBox();
        // бокс для catch
        VmBaseInstructionsBox catchBox = new VmBaseInstructionsBox();
        // компилируем try
        WattCompiler.code.writeTo(
                tryBox
        );
        tryNode.compile();
        WattCompiler.code.endWrite();
        // компилируем catch
        WattCompiler.code.writeTo(
                catchBox
        );
        catchNode.compile();
        WattCompiler.code.endWrite();
        // итоговая компиляция
        WattCompiler.code.visitInstruction(
            new VmInstructionTry(
                catchVariableName.asAddress(),
                tryBox,
                catchBox,
                catchVariableName.value
            )
        );
    }
}
