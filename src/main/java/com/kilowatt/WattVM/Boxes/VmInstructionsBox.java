package com.kilowatt.WattVM.Boxes;

import com.kilowatt.WattVM.Instructions.VmInstruction;

/*
Контейнер для инструкций
 */
public interface VmInstructionsBox {
    void visitInstr(VmInstruction instruction);
}
