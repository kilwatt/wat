package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionDefine;
import com.kea.KeaVM.Instructions.VmInstructionSet;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.Getter;

/*
Установка значения переменной
 */
@Getter
public class VarSetNode implements AccessNode {
    private final Node previous;
    private final Token name;
    private final Node value;

    public VarSetNode(Node previous, Token name, Node value) {
        this.previous = previous;
        this.name = name;
        this.value = value;
    }

    @Override
    public void compile() {
        VmBaseInstructionsBox argsBox = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(argsBox);
        value.compile();
        KeaCompiler.code.endWrite();
        System.out.println(argsBox.getVarContainer());
        KeaCompiler.code.visitInstruction(
                new VmInstructionSet(
                        new VmAddress(name.fileName, name.line),
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
