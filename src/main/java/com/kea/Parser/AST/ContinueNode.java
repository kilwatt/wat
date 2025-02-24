package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionLoopEnd;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Следующая итерация цикла
 */
@Getter
@AllArgsConstructor
public class ContinueNode implements Node {
    private final Token location;

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionLoopEnd(
                        new VmAddress(location.getFileName(), location.getLine()),
                        true
                )
        );
    }
}
