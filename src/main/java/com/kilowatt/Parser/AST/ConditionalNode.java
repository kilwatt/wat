package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.VmInstructionCondOp;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;

/*
Условие
 */
@AllArgsConstructor
public class ConditionalNode implements Node {
    private final Node left;
    private final Node right;
    private final Token operator;

    @Override
    public void compile() {
        left.compile();
        right.compile();
        WattCompiler.code.visitInstruction(
                new VmInstructionCondOp(
                        new VmAddress(operator.getFileName(), operator.getLine()),
                        operator.value
                )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        left.analyze(analyzer);
        right.analyze(analyzer);
    }
}
