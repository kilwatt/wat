package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionCondOp;
import com.kea.KeaVM.Instructions.VmInstructionLoopEnd;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
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
        KeaCompiler.code.visitInstruction(
                new VmInstructionLoopEnd(
                        location.asAddress(),
                        false
                )
        );
    }
}
