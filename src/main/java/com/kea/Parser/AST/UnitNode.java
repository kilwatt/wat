package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.Instructions.VmInstructionDefineUnit;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Опредление юнит
 */
@AllArgsConstructor
public class UnitNode implements Node {
    private final Token name;
    private final ArrayList<Node> body;

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefineUnit(name.asAddress(), name.value, compileBody())
        );
    }

    private VmBaseInstructionsBox compileBody() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        for (Node node : body) {
            node.compile();
        }
        KeaCompiler.code.endWrite();
        return box;
    }
}
