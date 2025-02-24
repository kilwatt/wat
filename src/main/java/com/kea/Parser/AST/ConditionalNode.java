package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Условие
 */
@AllArgsConstructor
public class ConditionalNode implements Node {
    private final Node left;
    private final Node right;
    private final Token operator;

    @Override
    public void compile() {
        
    }
}
