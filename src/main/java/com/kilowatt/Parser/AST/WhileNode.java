package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.VmInstructionLoop;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import lombok.AllArgsConstructor;

/*
While
 */
@AllArgsConstructor
public class WhileNode implements Node {
    private final Token location;
    private final BlockNode node;
    private final Node logical;

    @Override
    public void compile() {
        VmInstructionLoop loop = new VmInstructionLoop(new VmAddress(location.fileName, location.line));
        WattCompiler.code.writeTo(loop.getInstructions());
        compileLogical();
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(loop);
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.push(this);
        logical.analyze(analyzer);
        node.analyze(analyzer);
        analyzer.pop();
    }

    private void compileLogical() {
        new IfNode(
                location, node, logical,
                new IfNode(
                        location,
                        BlockNode.of(new BreakNode(location)),
                        new BoolNode(
                                new Token(TokenType.BOOL, "true",
                                        location.getLine(), location.getFileName()
                                )),
                        null
                )
        ).compile();
    }
}
