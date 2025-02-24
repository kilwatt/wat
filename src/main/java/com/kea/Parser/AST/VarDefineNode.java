package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionCall;
import com.kea.KeaVM.Instructions.VmInstructionDefine;
import com.kea.KeaVM.Instructions.VmInstructionDefineFn;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.Getter;

/*
Определние переменной
 */
@Getter
public class VarDefineNode implements AccessNode {
    private final AccessNode previous;
    private final Token name;
    private final Node value;

    public VarDefineNode(AccessNode previous, Token name, Node value) {
        this.previous = previous;
        this.name = name;
        this.value = value;
    }

    @Override
    public void compile() {
        // аргументы
        VmBaseInstructionsBox argsBox = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(argsBox);
        value.compile();
        KeaCompiler.code.endWrite();
        // компиляция
        if (previous != null) previous.compile();
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        name.asAddress(),
                        name.getValue(),
                        previous != null,
                        argsBox
                )
        );
    }

    @Override
    public boolean shouldPushResult() {
        return false;
    }

    @Override
    public void setShouldPushResult(boolean value) {
        return;
    }
}
