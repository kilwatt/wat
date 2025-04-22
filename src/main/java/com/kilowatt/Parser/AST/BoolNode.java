package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import lombok.Getter;

/*
Бул
 */
@Getter
public class BoolNode implements Node {
    private final Token value;

    public BoolNode(Token value) {
        this.value = value;
    }

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionPush(
                new VmAddress(value.getFileName(), value.getLine()),
                Boolean.parseBoolean(value.getValue())
            )
        );
    }
}
