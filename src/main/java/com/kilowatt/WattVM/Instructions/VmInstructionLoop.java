package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmLoopBreak;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
LOOP для VM
 */
@SuppressWarnings("UnnecessaryContinue")
@Getter
public class VmInstructionLoop implements VmInstruction {
    // адресс
    private final VmAddress address;
    // инструкции
    private final VmChunk body = new VmChunk();

    public VmInstructionLoop(VmAddress address) {
        this.address = address;
    }

    public static <T> T cast(Class<T> clazz, Object o) {
        return clazz.cast(o);
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        // создаём новую таблицу для цикла
        VmTable<String, Object> loopTable = new VmTable<>();
        loopTable.setRoot(table);
        // цикл
        while (true) {
            try {
                body.run(vm, loopTable);
            } catch (VmLoopBreak loopEnd) {
                if (!loopEnd.isCurrentIteration()) {
                    break;
                } else {
                    continue;
                }
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "LOOP()");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : body.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "START_LOOP(" + body + ")";
    }
}
