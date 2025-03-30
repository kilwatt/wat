package com.kilowatt.Parser.AST;

import com.kilowatt.Semantic.SemanticAnalyzer;

public interface Node {
    void compile();
    default void analyze(SemanticAnalyzer analyzer) {}
}
