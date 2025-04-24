package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmTrait;
import com.kilowatt.WattVM.Entities.VmTraitFunction;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineTrait;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Опредление трэйта
 */
@AllArgsConstructor
@Getter
public class TraitNode implements Node {
    // имя
    private final Token name;
    // полное имя
    private final Token fullName;
    // список функций
    private final ArrayList<TraitFn> traitFns;

    // функция трэйта
    @AllArgsConstructor
    @Getter
    public static class TraitFn {
        private final Token name;
        private final int paramsAmount;
        private final FnNode defaultImpl;
    }

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionDefineTrait(
                name.asAddress(),
                compileTrait()
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
                "couldn't create units outside global scope",
                "move the definition to the global code."
            );
        }
        // проверяем дочерние узлы
        analyzer.push(this);
        for (TraitFn traitFn : traitFns) {
            if (traitFn.getDefaultImpl() != null) {
                traitFn.getDefaultImpl().analyze(analyzer);
            }
        }
        analyzer.pop();
    }

    private VmTrait compileTrait() {
        ArrayList<VmTraitFunction> functions = new ArrayList<>();
        for (TraitFn traitFn : traitFns) {
            if (traitFn.getDefaultImpl() == null) {
                functions.add(new VmTraitFunction(
                    traitFn.getName().value,
                    traitFn.paramsAmount,
                    null
                ));
            } else {
                VmChunk implChunk = new VmChunk();
                WattCompiler.code.writeTo(implChunk);
                traitFn.getDefaultImpl().compile();
                WattCompiler.code.endWrite();
                functions.add(new VmTraitFunction(
                    traitFn.getName().value,
                    traitFn.paramsAmount,
                    implChunk
                ));
            }
        }
        return new VmTrait(
            name.value,
            fullName.value,
            functions
        );
    }
}
