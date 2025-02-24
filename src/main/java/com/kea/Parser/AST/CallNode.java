package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionCall;
import com.kea.KeaVM.Instructions.VmInstructionLoopEnd;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    @Override
    public void compile() {
        // аргументы
        VmBaseInstructionsBox argsBox = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(argsBox);
        for (Node node : args) {
            node.compile();
        }
        KeaCompiler.code.endWrite();
        // компиляция
        if (previous != null) previous.compile();
        KeaCompiler.code.visitInstruction(
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
    public boolean shouldPushResult() {
        return this.shouldPushResult;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        this.shouldPushResult = value;
    }
}
