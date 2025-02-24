package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.Getter;

/*
Бул
 */
@Getter
public class BoolNode implements Node {
    private final Token value;

    public BoolNode(Token value) {
        this.value = value;
    }

    @Override
    public void compile() {

    }
}
