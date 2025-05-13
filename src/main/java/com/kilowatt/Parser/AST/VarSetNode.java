package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstructionSet;
import com.kilowatt.Lexer.Token;
import lombok.Getter;

/*
Установка значения переменной
 */
@Getter
public class VarSetNode implements AccessNode {
    private final Node previous;
    private final Token name;
    private final Node value;

    public VarSetNode(Node previous, Token name, Node value) {
        this.previous = previous;
        this.name = name;
        this.value = value;
    }

    @Override
    public void compile() {
        // аргументы
        VmChunk valueChunk = new VmChunk();
        WattCompiler.code.writeTo(valueChunk);
        value.compile();
        WattCompiler.code.endWrite();
        // компиляция
        if (previous != null) previous.compile();
        WattCompiler.code.visitInstruction(
            new VmInstructionSet(
                name.asAddress(),
                name.getValue(),
                previous != null,
                valueChunk
            )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.analyze(previous);
        value.analyze(analyzer);
    }

    @Override
    public boolean shouldPushResult() {
        return false;
    }

    @Override
    public void setShouldPushResult(boolean value) {
    }
}
