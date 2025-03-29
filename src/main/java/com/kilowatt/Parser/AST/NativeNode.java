package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.*;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;

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
        WattCompiler.code.writeTo(value);
        WattCompiler.code.visitInstruction(
                new VmInstructionLoad(
                        name.asAddress(),
                        "__refl__",
                        false,
                        true
                )
        );
        VmBaseInstructionsBox args = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(args);
        WattCompiler.code.visitInstruction(
                new VmInstructionPush(
                        javaName.asAddress(),
                        javaName.value
                )
        );
        WattCompiler.code.visitInstruction(
                new VmInstructionPush(
                        javaName.asAddress(),
                        new WattList()
                )
        );
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(
                new VmInstructionCall(
                        name.asAddress(),
                        "reflect",
                        args,
                        true,
                        true
                )
        );
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        name.asAddress(),
                        name.value,
                        false,
                        value
                )
        );
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {

    }
}
