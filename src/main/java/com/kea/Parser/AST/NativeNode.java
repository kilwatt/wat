package com.kea.Parser.AST;

import com.kea.Compiler.Builtins.Libraries.Collections.KeaList;
import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Entities.VmFunction;
import com.kea.KeaVM.Instructions.*;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Нативная функция
 */
@AllArgsConstructor
public class NativeNode implements Node {
    private final Token name;
    private final Token javaName;

    @Override
    public void compile() {
        VmBaseInstructionsBox value = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(value);
        KeaCompiler.code.visitInstruction(
                new VmInstructionLoad(
                        name.asAddress(),
                        "reflection",
                        false,
                        true
                )
        );
        VmBaseInstructionsBox args = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(args);
        KeaCompiler.code.visitInstruction(
                new VmInstructionPush(
                        javaName.asAddress(),
                        javaName.value
                )
        );
        KeaCompiler.code.visitInstruction(
                new VmInstructionPush(
                        javaName.asAddress(),
                        new KeaList()
                )
        );
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(
                new VmInstructionCall(
                        name.asAddress(),
                        "reflect",
                        args,
                        true,
                        true
                )
        );
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        name.asAddress(),
                        name.value,
                        false,
                        value
                )
        );
    }
}
