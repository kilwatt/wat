package com.kea.Parser.AST;

import lombok.AllArgsConstructor;
import lombok.Setter;

/*
If
 */
@AllArgsConstructor
public class IfNode implements Node {
    private final BlockNode node;
    private final Node logical;
    @Setter
    private IfNode elseNode;

    @Override
    public void compile() {

    }
}
