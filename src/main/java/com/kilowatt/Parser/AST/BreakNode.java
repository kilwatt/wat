package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionLoopEnd;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Выход из цикла
 */
@Getter
@AllArgsConstructor
public class BreakNode implements Node {
    private final Token location;
    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
                new VmInstructionLoopEnd(
                        location.asAddress(),
                        false
                )
        );
    }
}
