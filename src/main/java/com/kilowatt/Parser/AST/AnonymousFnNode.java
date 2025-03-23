package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.Watt;
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
        ArrayList<String> parsed = new ArrayList<>();
        for (Token tk : parameters) {
            parsed.add(tk.value);
        }
        VmFunction fn = new VmFunction("$lambda", parsed, location.asAddress());
        WattCompiler.code.writeTo(fn);
        node.compile();
        WattCompiler.code.endWrite();
        return fn;
    }
}
