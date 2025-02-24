package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionInstance;
import com.kea.Lexer.Token;
import lombok.Getter;

import java.util.ArrayList;

/*
Создание экземпляра класса.
 */
@Getter
public class NewInstanceNode implements AccessNode {
    private final Token name;
    private final ArrayList<Node> args;

    public NewInstanceNode(Token name, ArrayList<Node> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(new VmInstructionInstance(name.asAddress(), name.value, compileArgs()));
    }

    private VmBaseInstructionsBox compileArgs() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        for (Node node : args) {
            node.compile();
        }
        KeaCompiler.code.endWrite();
        return box;
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
