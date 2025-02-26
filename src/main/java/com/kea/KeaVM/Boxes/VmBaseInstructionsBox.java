package com.kea.KeaVM.Boxes;


import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.Instructions.VmInstructionReturn;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

import java.util.ArrayList;

/*
Временный контейнер переменных для вызова функции
 */
@Getter
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class VmBaseInstructionsBox implements VmInstructionsBox {
    // контейнер
    private final ArrayList<VmInstruction> varContainer = new ArrayList<>();

    /**
     * Выполняет инструкцию
     * @param instr - инструкция
     */
    @Override
    public void visitInstr(VmInstruction instr) {
        this.varContainer.add(instr);
    }

    // выполнение и получение результата
    public Object runAndGet(KeaVM vm, VmFrame<String, Object> frame)  {
        for (VmInstruction instr : varContainer) {
            instr.run(vm, frame);
        }
        return vm.pop();
    }

    // запуск простой
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
        for (VmInstruction instr : varContainer) {
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
        return getVarContainer().toString();
    }
}
