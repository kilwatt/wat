package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Entities.VmFunction;
import com.kea.KeaVM.Instructions.VmInstructionDefine;
import com.kea.KeaVM.Instructions.VmInstructionDefineFn;
import com.kea.KeaVM.Instructions.VmInstructionLoopEnd;
import com.kea.KeaVM.Instructions.VmInstructionMakeClosure;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.awt.*;
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
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefineFn(
                        name.asAddress(),
                        compileFn()
                )
        );
        KeaCompiler.code.visitInstruction(
                new VmInstructionMakeClosure(name.asAddress(), name.value)
        );
    }

    public VmFunction compileFn() {
        ArrayList<String> parsed = new ArrayList<>();
        for (Token tk : parameters) {
            parsed.add(tk.value);
        }
        VmFunction fn = new VmFunction(name.value, parsed, name.asAddress());
        KeaCompiler.code.writeTo(fn);
        node.compile();
        KeaCompiler.code.endWrite();
        return fn;
    }
}
