package com.kea.Parser.AST;

import lombok.AllArgsConstructor;

/*
While
 */
@AllArgsConstructor
public class WhileNode implements Node {
    private final BlockNode node;
    private final Node logical;

    @Override
    public void compile() {

    }
}
