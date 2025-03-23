package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionSet;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
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
        // аргументы
        VmBaseInstructionsBox argsBox = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(argsBox);
        value.compile();
        WattCompiler.code.endWrite();
        // компиляция
        if (previous != null) previous.compile();
        WattCompiler.code.visitInstruction(
                new VmInstructionSet(
                        new VmAddress(name.fileName, name.line),
                        name.getValue(),
                        previous != null,
                        argsBox
                )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.analyze(previous);
        value.analyze(analyzer);
    }

    @Override
    public boolean shouldPushResult() {
        return false;
    }

    @Override
    public void setShouldPushResult(boolean value) {
    }
}
