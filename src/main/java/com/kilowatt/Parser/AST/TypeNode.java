package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineType;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction(
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
        WattCompiler.code.writeTo(box);
        for (Node node : locals) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        return box;
    }
}
