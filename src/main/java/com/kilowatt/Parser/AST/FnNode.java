package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Instructions.VmInstructionDefineFn;
import com.kilowatt.WattVM.Instructions.VmInstructionMakeClosure;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Функция
 */
@AllArgsConstructor
public class FnNode implements Node {
    private final BlockNode node;
    private final Token name;
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

    public VmFunction compileFn() {
        ArrayList<String> parsed = new ArrayList<>();
        for (Token tk : parameters) {
            parsed.add(tk.value);
        }
        VmFunction fn = new VmFunction(name.value, parsed, name.asAddress());
        WattCompiler.code.writeTo(fn);
        node.compile();
        WattCompiler.code.endWrite();
        return fn;
    }
}
