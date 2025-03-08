package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Entities.VmFunction;
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
