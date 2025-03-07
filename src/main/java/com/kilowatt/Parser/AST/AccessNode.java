package com.kilowatt.Parser.AST;

public interface AccessNode extends Node {
    boolean shouldPushResult();
    void setShouldPushResult(boolean value);
}
