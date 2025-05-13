package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineFn;
import com.kilowatt.WattVM.Instructions.VmInstructionMakeClosure;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/*
Функция
 */
@AllArgsConstructor
@Getter
public class FnNode implements Node {
    private final BlockNode node;
    private final Token name;
    @Setter
    private Token fullName;
    private final ArrayList<Token> parameters;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
            new VmInstructionDefineFn(
                name.asAddress(),
                compileFn()
            )
        );
        WattCompiler.code.visitInstruction(
            new VmInstructionMakeClosure(name.asAddress(), name.value)
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        // анализ самой функции
        if (analyzer.hasTop(TypeNode.class) || analyzer.hasTop(UnitNode.class)) {
            if (name.value.equals("init") && !parameters.isEmpty()) {
                throw new WattSemanticError(
                    name.asAddress(),
                    "couldn't create init function with args.",
                    "check your code."
                );
            }
        }
        // анализ тела
        analyzer.push(this);
        node.analyze(analyzer);
        analyzer.pop();
    }

    public VmFunction compileFn() {
        ArrayList<String> params = new ArrayList<>();
        for (Token tk : parameters) {
            params.add(tk.value);
        }
        VmChunk body = new VmChunk();
        WattCompiler.code.writeTo(body);
        node.compile();
        WattCompiler.code.endWrite();
        if (fullName == null) return new VmFunction(name.value, null, body, params, name.asAddress());
        return new VmFunction(name.value, fullName.value, body, params, name.asAddress());
    }
}
