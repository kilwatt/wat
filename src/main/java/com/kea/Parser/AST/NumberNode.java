package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.Getter;

/*
Число
 */
@Getter
public class NumberNode implements Node {
    private final Token value;

    public NumberNode(Token value) {
        this.value = value;
    }

    @Override
    public void compile() {

    }
}
