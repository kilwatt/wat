package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

/*
Range
 */
@AllArgsConstructor
public class RangeNode implements Node {
    private final Node from;
    private final Node to;

    @Override
    public void compile() {

    }
}
