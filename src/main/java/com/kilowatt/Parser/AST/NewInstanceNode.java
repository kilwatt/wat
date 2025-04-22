package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstructionInstance;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction(new VmInstructionInstance(name.asAddress(), name.value, compileConstructor()));
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        for (Node node : args) {
            node.analyze(analyzer);
        }
    }

    private VmChunk compileConstructor() {
        VmChunk constructorChunk = new VmChunk();
        WattCompiler.code.writeTo(constructorChunk);
        for (Node node : args) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        return constructorChunk;
    }

    @Override
    public boolean shouldPushResult() {
        return false;
    }

    @Override
    public void setShouldPushResult(boolean value) {}
}
