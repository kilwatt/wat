package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmThrowable;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmTable;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
TRY для VM
 */
@Getter
@AllArgsConstructor
public class VmInstructionTry implements VmInstruction {
    // адресс
    private final VmAddress address;
    // инструкции
    private final VmChunk tryBody;
    private final VmChunk catchBody;
    // имя переменной для catch
    private final String catchVariableName;
    
    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        try {
            // выполняем тело
            tryBody.run(vm, table);
        } catch (RuntimeException error) {
            // устанавливаем переменную
            if (error instanceof VmThrowable throwable) {
                table.define(address, catchVariableName, throwable);
            } else {
                table.define(address, catchVariableName, new VmThrowable(error));
            }
            // выполняем тело исключения
            catchBody.run(vm, table);
            // удаляем переменную
            table.getValues().remove(catchVariableName);
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "TRY()");
        VmCodeDumper.dumpLine(indent + 1, "- BODY:");
        for (VmInstruction instruction : tryBody.getInstructions()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent, "ON_CATCH()");
        VmCodeDumper.dumpLine(indent + 1, "- BODY:");
        for (VmInstruction instruction : catchBody.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "TRY(try: " + tryBody + ", catch: " + catchBody + ", var: " + catchVariableName + ")";
    }
}
