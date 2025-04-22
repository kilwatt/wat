package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmChunk;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Instructions.VmInstructionDuplicate;
import com.kilowatt.WattVM.Instructions.VmInstructionMakeClosure;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Анонимная функция
 */
@AllArgsConstructor
public class AnonymousFnNode implements Node {
    private final Token location;
    private final BlockNode node;
    private final ArrayList<Token> parameters;

    @Override
    public void compile() {
        WattCompiler.code.visitInstruction(
                new VmInstructionPush(
                        location.asAddress(),
                        compileFn()
                )
        );
        WattCompiler.code.visitInstruction(
                new VmInstructionDuplicate(
                        location.asAddress()
                )
        );
        WattCompiler.code.visitInstruction(
                new VmInstructionMakeClosure(
                        location.asAddress()
                )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
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
        return new VmFunction("$lambda", body, params, location.asAddress());
    }
}
