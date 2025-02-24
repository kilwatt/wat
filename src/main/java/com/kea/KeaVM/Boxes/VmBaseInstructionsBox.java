package com.kea.KeaVM.Boxes;


import com.kea.KeaVM.Instructions.VmInstruction;
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

    public Object exec(KeaVM vm, VmFrame<String, Object> frame)  {
        for (VmInstruction instr : varContainer) {
            instr.run(vm, frame);
        }
        return vm.pop();
    }
}
