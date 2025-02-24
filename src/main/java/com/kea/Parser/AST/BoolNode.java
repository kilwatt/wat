package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
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
        KeaCompiler.code.visitInstruction(
                new VmInstructionPush(
                        new VmAddress(value.getFileName(), value.getLine()),
                        Boolean.parseBoolean(value.getValue())
                )
        );
    }
}
