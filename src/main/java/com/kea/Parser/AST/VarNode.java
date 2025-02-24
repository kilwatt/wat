package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Получение переменной
 */
@Getter
public class VarNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private boolean shouldPushResult = false;

    public VarNode(AccessNode previous, Token name) {
        this.previous = previous;
        this.name = name;
    }

    @Override
    public void compile() {

    }

    @Override
    public boolean shouldPushResult() {
        return shouldPushResult;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        this.shouldPushResult = value;
    }
}
