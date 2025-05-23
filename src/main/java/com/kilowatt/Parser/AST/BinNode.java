package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.VmInstructionBinOp;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Бинарная операция
 */
@Getter
@AllArgsConstructor
public class BinNode implements Node {
    private final Node left;
    private final Node right;
    private final Token operator;

    @Override
    public void compile() {
        left.compile();
        right.compile();
        WattCompiler.code.visitInstruction(
            new VmInstructionBinOp(
                operator.asAddress(),
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
