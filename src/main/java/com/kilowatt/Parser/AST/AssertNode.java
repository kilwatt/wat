package com.kilowatt.Parser.AST;

import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import com.kilowatt.Semantic.SemanticAnalyzer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
Assert
 */
@Slf4j
@AllArgsConstructor
public class AssertNode implements Node {
    private final Token location;
    private final Node logical;

    @Override
    public void compile() {
        new IfNode(location, BlockNode.empty(), logical,
            new IfNode(location,
                BlockNode.of(
                    new CallNode(null, new Token(TokenType.ID, "error", location.line, location.fileName),
                        CallNode.args(
                            new StringNode(
                                    new Token(TokenType.TEXT, "Assertion error.", location.line, location.fileName)
                            )
                        )
                    )
                ),
                new BoolNode(
                        new Token(TokenType.BOOL, "true",
                                location.getLine(), location.getFileName()
                )),
                null
            )
        ).compile();
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        logical.analyze(analyzer);
    }
}
