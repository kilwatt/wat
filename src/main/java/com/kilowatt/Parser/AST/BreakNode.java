package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.VmInstructionLoopEnd;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Выход из цикла
 */
@Getter
@AllArgsConstructor
public class BreakNode implements Node {
    private final Token location;
    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionLoopEnd(
                location.asAddress(),
                false
            )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        if (!analyzer.hasTop(WhileNode.class) &&
            !analyzer.hasTop(ForNode.class)) {
            throw new WattSemanticError(
                location.asAddress(),
                "couldn't use break outside a loop",
                "check your code."
            );
        }
    }
}
