package com.kilowatt.Parser.AST;

import com.kilowatt.Semantic.SemanticAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Блок
 */
@AllArgsConstructor
@Getter
public class BlockNode implements Node {
    private final ArrayList<Node> nodes;

    @Override
    public void compile() {
        for (Node node : nodes) {
            node.compile();
        }
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        for (Node node : nodes) {
            node.analyze(analyzer);
        }
    }

    public static BlockNode of(Node... nodes) {
        return new BlockNode(new ArrayList<>(List.of(nodes)));
    }
    public static BlockNode empty() { return new BlockNode(new ArrayList<>()); }
}
