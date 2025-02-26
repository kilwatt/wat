package com.kea.Parser.AST;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
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

    public static BlockNode of(Node... nodes) {
        return new BlockNode(new ArrayList<>(List.of(nodes)));
    }
    public static BlockNode empty() { return new BlockNode(new ArrayList<>()); }
}
