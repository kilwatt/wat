package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineUnit;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Опредление юнит
 */
@AllArgsConstructor
@Getter
public class UnitNode implements Node {
    // имя
    private Token name;
    // полное имя
    private Token fullName;
    // тело
    private final ArrayList<Node> body;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionDefineUnit(
                name.asAddress(),
                name.value,
                fullName.value,
                compileUnit()
            )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        // смотрим на правильность определения
        if (analyzer.top() != null) {
            throw new WattSemanticError(
                name.getLine(),
                name.getFileName(),
                "couldn't create units outside global code",
                "move the definition to the global code."
            );
        }
        // проверяем дочерние узлы
        analyzer.push(this);
        for (Node node : body) {
            node.analyze(analyzer);
        }
        analyzer.pop();
    }

    private VmBaseInstructionsBox compileUnit() {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(box);
        for (Node node : body) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        return box;
    }
}
