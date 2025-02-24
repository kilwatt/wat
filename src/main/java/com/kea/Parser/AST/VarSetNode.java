package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.Getter;

/*
Установка значения переменной
 */
@Getter
public class VarSetNode implements AccessNode {
    private final Node previous;
    private final Token name;
    private final Node value;

    public VarSetNode(Node previous, Token name, Node value) {
        this.previous = previous;
        this.name = name;
        this.value = value;
    }

    @Override
    public void compile() {

    }

    @Override
    public boolean shouldPushResult() {
        return false;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        return;
    }
}
