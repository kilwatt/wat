package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmThrowable;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.VmCodeDumper;
import com.kilowatt.WattVM.VmFrame;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Нода выкидывания исключения
 */
@AllArgsConstructor
@Getter
public class ThrowNode implements Node {
    // значение
    private Node value;

    @Override
    public void compile() {
        // бокс со значением
        VmBaseInstructionsBox valueBox = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(valueBox);
        value.compile();
        WattCompiler.code.endWrite();
        // итоговая компиляция
        WattCompiler.code.visitInstruction(
            new VmInstruction() {
                @Override
                public void run(WattVM vm, VmFrame<String, Object> scope) {
                    throw new VmThrowable(valueBox.runAndGet(vm, scope));
                }

                @Override
                public void print(int indent) {
                    VmCodeDumper.dumpLine(indent, "THROW(), BODY:");
                    for (VmInstruction instruction : valueBox.getInstructionContainer()) {
                        instruction.print(indent + 1);
                    }
                }

                @Override
                public String toString() {
                    return "THROW(value: " + valueBox + ")";
                }
            }
        );
    }
}
