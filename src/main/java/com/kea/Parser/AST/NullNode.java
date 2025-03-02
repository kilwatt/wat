package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NullNode implements Node {
    private final Token location;

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionPush(
                        location.asAddress(),
                        null
                )
        );
    }
}
