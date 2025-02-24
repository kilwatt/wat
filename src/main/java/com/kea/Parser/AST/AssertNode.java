package com.kea.Parser.AST;

import lombok.AllArgsConstructor;
import lombok.Setter;

/*
Assert
 */
@AllArgsConstructor
public class AssertNode implements Node {
    private final Node logical;

    @Override
    public void compile() {

    }
}
