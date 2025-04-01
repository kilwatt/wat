package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineType;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Опредление типа
 */
@AllArgsConstructor
@Getter
public class TypeNode implements Node {
    // имя
    private final Token name;
    // полное имя
    private final Token fullName;
    // тело
    private final ArrayList<Node> body;
    // конструктор
    private final ArrayList<Token> constructor;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionDefineType(
                name.asAddress(),
                name.value,
                fullName.value,
                compileType()
            )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.push(this);
        for (Node node : body) {
            node.analyze(analyzer);
        }
        analyzer.pop();
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
        for (Node node : body) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        return box;
    }
}
