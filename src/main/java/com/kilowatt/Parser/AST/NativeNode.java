package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.*;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/*
Нативная функция
 */
@AllArgsConstructor
public class NativeNode implements Node {
    private final Token name;
    private final Token fullName;
    private final Token javaName;

    @Override
    public void compile() {
        // компилируем функцию
        VmBaseInstructionsBox value = compileFn();
        // дефайн по имени
        WattCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        name.asAddress(),
                        name.value,
                        false,
                        value
                )
        );
        // дефайн по полному имени
        WattCompiler.code.visitInstruction(
                new VmInstructionDefine(
                        fullName.asAddress(),
                        fullName.value,
                        false,
                        value
                )
        );
    }

    private VmBaseInstructionsBox compileFn() {
        // компиляция
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
        new ListNode(name, new ArrayList<>()).compile();
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
        // возвращаем
        return value;
    }
}
