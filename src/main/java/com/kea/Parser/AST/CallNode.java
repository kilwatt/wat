package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
Вызов функции
 */
@Getter
public class CallNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private final List<Node> args;
    private boolean shouldPushResult = false;

    public CallNode(AccessNode previous, Token name, List<Node> args) {
        this.previous = previous;
        this.name = name;
        this.args = args;
    }

    @Override
    public void compile() {

    }

    @Override
    public boolean shouldPushResult() {
        return this.shouldPushResult;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        this.shouldPushResult = value;
    }
}
