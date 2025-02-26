package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Range
 */
@AllArgsConstructor
@Getter
public class RangeNode implements Node {
    private final Node from;
    private final Node to;
    private final boolean isDecrement;

    @Override
    public void compile() {
        throw new RuntimeException("no impl.");
    }
}
