package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Опредление типа
 */
@AllArgsConstructor
public class TypeNode implements Node {
    private final Token name;
    private final ArrayList<Node> locals;
    // private final ArrayList<Node> statics;

    @Override
    public void compile() {
        
    }
}
