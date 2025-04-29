package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
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
        VmChunk valueChunk = new VmChunk();
        WattCompiler.code.writeTo(valueChunk);
        forReturn.compile();
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(
            new VmInstructionReturn(
                valueChunk,
                location.asAddress()
            )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        if (analyzer.topIs(FnNode.class) ||
            analyzer.topIs(AnonymousFnNode.class)) {
            forReturn.analyze(analyzer);
        } else {
            throw new WattSemanticError(
                    location.getLine(),
                    location.getFileName(),
                    "couldn't use return outside a function",
                    "check your code."
            );
        }
    }
}
