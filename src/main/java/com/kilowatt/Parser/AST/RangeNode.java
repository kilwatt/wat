package com.kilowatt.Parser.AST;

import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import lombok.AllArgsConstructor;

/*
Рэндж
 */
@AllArgsConstructor
public class RangeNode implements Node {
    private final Token location;
    private final Node from;
    private final Node to;

    @Override
    public void compile() {
        new CallNode(
            null,
            new Token(
                TokenType.ID,
                "rng",
                location.getLine(),
                location.getFileName()
            ),
            CallNode.args(
                from,
                to
            ),
            true
        ).compile();
    }
}
