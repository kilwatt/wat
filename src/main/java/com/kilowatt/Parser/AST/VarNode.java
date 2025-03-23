package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.VmInstructionLoad;
import com.kilowatt.Lexer.Token;
import lombok.Getter;

/*
Получение переменной
 */
@Getter
public class VarNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private boolean shouldPushResult = false;

    public VarNode(AccessNode previous, Token name) {
        this.previous = previous;
        this.name = name;
    }

    public VarNode(AccessNode previous, Token name, boolean shouldPushResult) {
        this.previous = previous;
        this.name = name;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void compile() {
        if (previous != null) previous.compile();
        WattCompiler.code.visitInstruction(
                new VmInstructionLoad(
                        name.asAddress(),
                        name.getValue(),
                        previous != null,
                        shouldPushResult
                )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.analyze(previous);
    }

    @Override
    public boolean shouldPushResult() {
        return shouldPushResult;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        this.shouldPushResult = value;
    }
}
