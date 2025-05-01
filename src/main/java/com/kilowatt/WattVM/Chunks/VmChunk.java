package com.kilowatt.WattVM.Chunks;


import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

import java.util.ArrayList;

/*
Контейнер инструкций
 */
@Getter
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class VmChunk {
    // контейнер
    private final ArrayList<VmInstruction> instructions = new ArrayList<>();

    /**
     * Выполняет инструкцию
     * @param instr - инструкция
     */
    public void visitInstr(VmInstruction instr) {
        this.instructions.add(instr);
    }

    // выполнение и получение результата
    public Object runAndGet(WattVM vm, VmFrame<String, Object> frame, VmAddress address)  {
        for (VmInstruction instr : instructions) {
            instr.run(vm, frame);
        }
        return vm.pop(address);
    }

    // запуск простой
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        for (VmInstruction instr : instructions) {
            instr.run(vm, frame);
        }
    }

    // создание бокса инструкций из их списка
    public static VmChunk of(VmInstruction... instructions) {
        VmChunk chunk = new VmChunk();
        for (VmInstruction instruction : instructions) {
            chunk.visitInstr(instruction);
        }
        return chunk;
    }

    // в строку

    @Override
    public String toString() {
        return getInstructions().toString();
    }
}
