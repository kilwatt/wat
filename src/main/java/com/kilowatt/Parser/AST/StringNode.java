package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction(
                new VmInstructionPush(value.asAddress(), value.value)
        );
    }
}
