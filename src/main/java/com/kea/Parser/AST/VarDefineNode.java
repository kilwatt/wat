package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.Getter;

/*
Определние переменной
 */
@Getter
public class VarDefineNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private final Node value;

    public VarDefineNode(AccessNode previous, Token name, Node value) {
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
