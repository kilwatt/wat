package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmThrowable;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmCodeDumper;
import com.kilowatt.WattVM.VmFrame;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
TRY для VM
 */
@Getter
@AllArgsConstructor
public class VmInstructionTry implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // инструкции
    private final VmBaseInstructionsBox tryBody;
    private final VmBaseInstructionsBox catchBody;
    // имя переменной для catch
    private final String catchVariableName;
    
    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        try {
            // выполняем тело
            tryBody.run(vm, frame);
        } catch (RuntimeException e) {
            // устанавливаем переменную
            frame.define(addr, catchVariableName, new VmThrowable(e));
            // выполняем тело исключения
            catchBody.run(vm, frame);
            // удаляем переменную
            frame.getValues().remove(catchVariableName);
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "TRY()");
        VmCodeDumper.dumpLine(indent + 1, "- BODY:");
        for (VmInstruction instruction : tryBody.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent, "ON_CATCH()");
        VmCodeDumper.dumpLine(indent + 1, "- BODY:");
        for (VmInstruction instruction : catchBody.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "TRY(try: " + tryBody + ", catch: " + catchBody + ", var: " + catchVariableName + ")";
    }
}
