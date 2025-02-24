package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionCall;
import com.kea.KeaVM.Instructions.VmInstructionLoad;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Получение переменной
 */
@Getter
public class VarNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private boolean shouldPushResult = false;

    public VarNode(AccessNode previous, Token name) {
        this.previous = previous;
        this.name = name;
    }

    @Override
    public void compile() {
        if (previous != null) previous.compile();
        KeaCompiler.code.visitInstruction(
                new VmInstructionLoad(
                        name.asAddress(),
                        name.getValue(),
                        previous != null,
                        shouldPushResult
                )
        );
    }

    @Override
    public boolean shouldPushResult() {
        return shouldPushResult;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        this.shouldPushResult = value;
    }
}
