package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import com.kea.Lexer.TokenType;
import lombok.AllArgsConstructor;
/*
Assert
 */
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
}
