package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.ArrayList;

/*
Функция
 */
@AllArgsConstructor
public class FnNode implements Node {
    private final BlockNode node;
    private final Token name;
    private final ArrayList<Token> parameters;

    @Override
    public void compile() {

    }
}
