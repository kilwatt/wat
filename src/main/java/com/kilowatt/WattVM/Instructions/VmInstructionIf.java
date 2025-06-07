package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;
import lombok.Setter;

/*
IF для VM
 */
@Getter
public class VmInstructionIf implements VmInstruction {
    // адресс
    private final VmAddress address;
    // инструкции
    @Setter
    private VmChunk conditions = new VmChunk();
    private final VmChunk body = new VmChunk();
    // else
    @Setter
    private VmInstructionIf elseInstruction;

    public VmInstructionIf(VmAddress address) {
        this.address = address;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        // создаём новую таблицу
        VmTable<String, Object> ifTable = new VmTable<>();
        ifTable.setRoot(table);
        // выполняем инструкции условий
        Object val = conditions.runAndGet(vm, ifTable, address);
        // проверяем условия
        if (((Boolean) val)) {
            // выполняем тело
            body.run(vm, ifTable);
        }
        // в ином случае
        else {
            if (elseInstruction != null) {
                elseInstruction.run(vm, table);
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "IF():");
        VmCodeDumper.dumpLine(indent + 1, "CONDITIONS:");
        for (VmInstruction instruction : conditions.getInstructions()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : body.getInstructions()) {
            instruction.print(indent + 2);
        }
        if (elseInstruction != null) {
            VmCodeDumper.dumpLine(indent, "ELSE():");
            elseInstruction.print(indent);
        }
    }

    @Override
    public String toString() {
        return "IF_VALUE(conditions: " + conditions + ", body: " + body + ", else: " + elseInstruction + ")";
    }
}
