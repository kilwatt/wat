package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Entities.VmNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NullNode implements Node {
    private final Token location;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
                new VmInstructionPush(
                        location.asAddress(),
                        new VmNull()
                )
        );
    }
}
