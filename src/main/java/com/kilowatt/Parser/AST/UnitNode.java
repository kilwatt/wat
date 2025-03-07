package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineUnit;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction(
                new VmInstructionDefineUnit(name.asAddress(), name.value, compileBody())
        );
    }

    private VmBaseInstructionsBox compileBody() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(box);
        for (Node node : body) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        return box;
    }
}
