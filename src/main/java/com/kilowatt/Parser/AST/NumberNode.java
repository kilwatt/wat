package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction(
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
