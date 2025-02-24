package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.Getter;

import java.util.ArrayList;

/*
Создание экземпляра класса.
 */
@Getter
public class NewInstanceNode implements AccessNode {
    private final Token name;
    private final ArrayList<Node> args;

    public NewInstanceNode(Token name, ArrayList<Node> args) {
        this.name = name;
        this.args = args;
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
