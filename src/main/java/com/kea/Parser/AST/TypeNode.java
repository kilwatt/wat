package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Instructions.VmInstructionDefineType;
import com.kea.KeaVM.VmFrame;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Опредление типа
 */
@AllArgsConstructor
public class TypeNode implements Node {
    private final Token name;
    private final ArrayList<Node> locals;

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefineType(name.asAddress(), name.value, compileType())
        );
    }

    // компиляция типа
    private VmType compileType() {
        return new VmType(name.value, new ArrayList<>(), compileFields());
    }

    // компиляция полей
    private VmBaseInstructionsBox compileFields() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(box);
        for (Node node : locals) {
            node.compile();
        }
        KeaCompiler.code.endWrite();
        return box;
    }
}
