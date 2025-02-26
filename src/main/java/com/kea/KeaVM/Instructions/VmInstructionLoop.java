package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
LOOP для VM
 */
@Getter
public class VmInstructionLoop implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // инструкции
    private final VmBaseInstructionsBox instructions = new VmBaseInstructionsBox();

    public VmInstructionLoop(VmAddress addr) {
        this.addr = addr;
    }

    public static <T> T cast(Class<T> clazz, Object o) {
        return clazz.cast(o);
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        while (true) {
            try {
                instructions.run(vm, frame);
            } catch (VmInstructionLoopEnd loopEnd) {
                if (!loopEnd.isCurrentIteration()) {
                    break;
                } else {
                    continue;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "START_LOOP(" + instructions + ")";
    }
}
