package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
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
    private final Token fullName;
    private final ArrayList<Node> body;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
                new VmInstructionDefineUnit(name.asAddress(), name.value, fullName.value, compileUnit())
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
