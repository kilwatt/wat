package com.kea.KeaVM.Boxes;

import com.kea.KeaVM.Instructions.VmInstruction;

/*
Контейнер для инструкций
 */
public interface VmInstructionsBox {
    void visitInstr(VmInstruction instruction);
}
