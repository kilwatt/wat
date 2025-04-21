package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Instructions.*;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Цикл for
 */
@AllArgsConstructor
public class ForNode implements Node {
    private final BlockNode body;
    private final Token name;
    private final Node iterable;

    @Override
    public void compile() {
        compileIterator();
        VmInstructionLoop loop = new VmInstructionLoop(new VmAddress(name.fileName, name.line));
        WattCompiler.code.writeTo(loop.getInstructions());
        compileBody();
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(loop);
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.push(this);
        iterable.analyze(analyzer);
        body.analyze(analyzer);
        analyzer.pop();
    }

    private void compileIterator() {
        // итератор
        Token iteratorName = new Token(
            TokenType.ID,
            "@"+name.value,
            name.getLine(),
            name.getFileName()
        );
        // дефайн итератора
        new VarDefineNode(
            null,
            iteratorName,
            iterable
        ).compile();
    }

    private void compileBody() {
        // итератор
        Token iteratorName = new Token(
                TokenType.ID,
                "@"+name.value,
                name.getLine(),
                name.getFileName()
        );
        // иф
        new IfNode(
            name,
            BlockNode.of(
                new VarDefineNode(
                    null,
                    name,
                    new CallNode(
                        new VarNode(
                            null,
                            iteratorName,
                            true
                        ),
                        new Token(
                            TokenType.ID,
                            "next",
                            name.getLine(),
                            name.getFileName()
                        ),
                        new ArrayList<>(),
                        true
                    )
                ),
                body
            ),
            new CallNode(
                new VarNode(
                    null,
                    iteratorName,
                    true
                ),
                new Token(
                    TokenType.ID,
                "hasNext",
                    name.getLine(),
                    name.getFileName()
                ),
                new ArrayList<>(),
                true
            ),
            new IfNode(
                name,
                BlockNode.of(
                    new BreakNode(name)
                ),
                new BoolNode(new Token(
                    TokenType.BOOL,
                    "true",
                    name.line,
                    name.fileName
                )),
                null
            )
        ).compile();
    }
}
