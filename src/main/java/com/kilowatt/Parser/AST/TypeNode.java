package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineType;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    // трэйты
    private final List<Token> traits;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionDefineType(
                name.asAddress(),
                compileType()
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
                "couldn't create types outside global scope",
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

    // компиляция типа
    private VmType compileType() {
        ArrayList<String> newConstructor = new ArrayList<>();
        for (Token token : constructor) {
            newConstructor.add(token.value);
        }
        ArrayList<String> newTraits = new ArrayList<>();
        for (Token token : traits) {
            newTraits.add(token.value);
        }
        return new VmType(name.value, fullName.value, newConstructor, newTraits, compileBody());
    }

    // компиляция тела
    private VmChunk compileBody() {
        VmChunk chunk = new VmChunk();
        WattCompiler.code.writeTo(chunk);
        for (Node node : body) {
            node.compile();
        }
        WattCompiler.code.endWrite();
        return chunk;
    }
}
