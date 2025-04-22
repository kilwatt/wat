package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmThrowable;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Storage.VmFrame;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Нода выкидывания исключения
 */
@AllArgsConstructor
@Getter
public class ThrowNode implements Node {
    // локация
    private final Token location;
    // значение
    private final Node value;

    @Override
    public void compile() {
        // бокс со значением
        VmChunk chunk = new VmChunk();
        WattCompiler.code.writeTo(chunk);
        value.compile();
        WattCompiler.code.endWrite();
        // адрес
        VmAddress address = location.asAddress();
        // итоговая компиляция
        WattCompiler.code.visitInstruction(
            new VmInstruction() {
                @Override
                public void run(WattVM vm, VmFrame<String, Object> scope) {
                    // объект
                    Object o = chunk.runAndGet(vm, address, scope);
                    // если это ошибка - сразу выкидываем
                    if (o instanceof WattError e) {
                        // выкидываем
                        throw (RuntimeException) e;
                    }
                    // иначе заворачиваем, а потом выкидываем
                    else {
                        // выкидываем
                        throw new VmThrowable(o);
                    }
                }

                @Override
                public void print(int indent) {
                    VmCodeDumper.dumpLine(indent, "THROW(), BODY:");
                    for (VmInstruction instruction : chunk.getInstructions()) {
                        instruction.print(indent + 1);
                    }
                }

                @Override
                public String toString() {
                    return "THROW(value: " + chunk + ")";
                }
            }
        );
    }
}
