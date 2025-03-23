package com.kilowatt.Parser.AST;

import com.kilowatt.Semantic.SemanticAnalyzer;

public interface AccessNode extends Node {
    boolean shouldPushResult();
    void setShouldPushResult(boolean value);
    void analyze(SemanticAnalyzer analyzer);
}
