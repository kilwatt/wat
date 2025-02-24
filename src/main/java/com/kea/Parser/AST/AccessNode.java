package com.kea.Parser.AST;

public interface AccessNode extends Node {
    boolean shouldPushResult();
    void setShouldPushResult(boolean value);
}
