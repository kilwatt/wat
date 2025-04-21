package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionCall;
import com.kilowatt.Lexer.Token;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Вызов функции
 */
@Getter
public class CallNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private final List<Node> args;
    private boolean shouldPushResult = false;

    public CallNode(AccessNode previous, Token name, List<Node> args) {
        this.previous = previous;
        this.name = name;
        this.args = args;
    }

    public CallNode(AccessNode previous, Token name, List<Node> args, boolean shouldPushResult) {
        this.previous = previous;
        this.name = name;
        this.args = args;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void compile() {
        // аргументы
        VmBaseInstructionsBox argsBox = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(argsBox);
        for (Node node : args) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        // компиляция
        if (previous != null) previous.compile();
        WattCompiler.code.visitInstruction(
                new VmInstructionCall(
                        name.asAddress(),
                        name.getValue(),
                        argsBox,
                        previous != null,
                        shouldPushResult
                )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.analyze(previous);
        for (Node node : args) {
            node.analyze(analyzer);
        }
    }

    @Override
    public boolean shouldPushResult() {
        return this.shouldPushResult;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        this.shouldPushResult = value;
    }

    public static List<Node> args(Node... args) {
        return new ArrayList<>(List.of(args));
    }
}
