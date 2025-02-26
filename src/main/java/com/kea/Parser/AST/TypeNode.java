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
    private final ArrayList<Token> constructor;

    @Override
    public void compile() {
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefineType(name.asAddress(), name.value, compileType())
        );
    }

    // компиляция типа
    private VmType compileType() {
        ArrayList<String> newConstructor = new ArrayList<>();
        for (Token token : constructor) {
            newConstructor.add(token.getValue());
        }
        return new VmType(name.value, newConstructor, compileFields());
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
