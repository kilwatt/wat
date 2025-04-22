package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstructionDefine;
import com.kilowatt.Lexer.Token;
import lombok.Getter;

/*
Определние переменной
 */
@Getter
public class VarDefineNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private final Node value;

    public VarDefineNode(AccessNode previous, Token name, Node value) {
        this.previous = previous;
        this.name = name;
        this.value = value;
    }

    @Override
    public void compile() {
        // аргументы
        VmChunk argsBox = new VmChunk();
        WattCompiler.code.writeTo(argsBox);
        value.compile();
        WattCompiler.code.endWrite();
        // компиляция
        if (previous != null) previous.compile();
        WattCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        name.asAddress(),
                        name.getValue(),
                        previous != null,
                        argsBox
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
        return;
    }
}
