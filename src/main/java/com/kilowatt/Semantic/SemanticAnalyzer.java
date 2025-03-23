package com.kilowatt.Semantic;

import com.kilowatt.Parser.AST.Node;

import java.util.Stack;

/*
Семантический анализтор
 */
public class SemanticAnalyzer {
    // стэк контекста
    private final Stack<Node> context = new Stack<>();

    // пуш в стэк контекста
    public void push(Node node) {
        context.push(node);
    }

    // поп из контекста
    public void pop() {
        context.pop();
    }

    // проверка на конкретную ноды
    public boolean topIs(Class<?> clazz) {
        return !context.isEmpty() && context.peek().getClass() == clazz;
    }

    // анализ
    public void analyze(Node node) {
        if (node == null) return;
        node.analyze(this);
    }
}
