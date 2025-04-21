package com.kilowatt.WattVM.Boxes;


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
public class VmBaseInstructionsBox implements VmInstructionsBox {
    // контейнер
    private final ArrayList<VmInstruction> instructionContainer = new ArrayList<>();

    /**
     * Выполняет инструкцию
     * @param instr - инструкция
     */
    @Override
    public void visitInstr(VmInstruction instr) {
        this.instructionContainer.add(instr);
    }

    // выполнение и получение результата
    public Object runAndGet(WattVM vm, VmAddress address, VmFrame<String, Object> frame)  {
        for (VmInstruction instr : instructionContainer) {
            instr.run(vm, frame);
        }
        return vm.pop(address);
    }

    // запуск простой
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        for (VmInstruction instr : instructionContainer) {
            instr.run(vm, frame);
        }
    }

    // создание бокса инструкций из их списка
    public static VmBaseInstructionsBox of(VmInstruction... instructions) {
        VmBaseInstructionsBox box = new VmBaseInstructionsBox();
        for (VmInstruction instruction : instructions) {
            box.visitInstr(instruction);
        }
        return box;
    }

    // в строку

    @Override
    public String toString() {
        return getInstructionContainer().toString();
    }
}
