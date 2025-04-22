package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Chunks.VmChunk;
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
        VmChunk tryChunk = new VmChunk();
        // бокс для catch
        VmChunk catchChunk = new VmChunk();
        // компилируем try
        WattCompiler.code.writeTo(
                tryChunk
        );
        tryNode.compile();
        WattCompiler.code.endWrite();
        // компилируем catch
        WattCompiler.code.writeTo(
                catchChunk
        );
        catchNode.compile();
        WattCompiler.code.endWrite();
        // итоговая компиляция
        WattCompiler.code.visitInstruction(
            new VmInstructionTry(
                catchVariableName.asAddress(),
                tryChunk,
                catchChunk,
                catchVariableName.value
            )
        );
    }
}
