package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.Getter;

/*
Строка
 */
@Getter
public class StringNode implements Node {
    private final Token value;

    public StringNode(Token value) {
        this.value = value;
    }

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionPush(value.asAddress(), value.value)
        );
    }
}
