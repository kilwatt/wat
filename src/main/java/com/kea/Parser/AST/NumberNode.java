package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.Getter;

/*
Число
 */
@Getter
public class NumberNode implements Node {
    private final Token value;

    public NumberNode(Token value) {
        this.value = value;
    }

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionPush(new VmAddress(value.getFileName(), value.getLine()), toNumber())
        );
    }

    private Number toNumber() {
        try {
            long longParsed = Long.parseLong(value.value);
            if (longParsed > 2147483647 || longParsed < -2147483647) {
                return longParsed;
            } else {
                return (int) longParsed;
            }
        } catch (NumberFormatException e) {
            return Float.parseFloat(value.value);
        }
    }
}
