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
        System.out.println("push: " + node);
        context.push(node);
    }

    // поп из контекста
    public void pop() {
        System.out.println("pop");
        context.pop();
    }

    // верхний элемент
    public Node top() {
        if (context.isEmpty()) return null;
        return context.peek();
    }

    // проверка на наличие ноды в иерархии
    public boolean hierarchyContains(Class<?> clazz) {
        for (Node node : context.stream().toList()) {
            if (node.getClass() == clazz) return true;
        }
        return false;
    }

    // анализ
    public void analyze(Node node) {
        if (node == null) return;
        node.analyze(this);
    }
}
