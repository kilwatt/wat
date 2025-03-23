package com.kilowatt.Parser.AST;

import com.kilowatt.Semantic.SemanticAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Range
 */
@AllArgsConstructor
@Getter
public class RangeNode implements Node {
    private final Node from;
    private final Node to;
    private final boolean isDecrement;

    @Override
    public void compile() {
        throw new RuntimeException("no impl.");
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        from.analyze(analyzer);
        to.analyze(analyzer);
    }
}
