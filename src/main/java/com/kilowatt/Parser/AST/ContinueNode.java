package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionLoopEnd;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction(
                new VmInstructionLoopEnd(
                        new VmAddress(location.getFileName(), location.getLine()),
                        true
                )
        );
    }
}
