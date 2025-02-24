package com.kea.Parser.AST;

import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.ArrayList;

/*
Блок
 */
@AllArgsConstructor
public class BlockNode implements Node {
    private final ArrayList<Node> nodes;

    @Override
    public void compile() {
        for (Node node : nodes) {
            node.compile();
        }
    }
}
