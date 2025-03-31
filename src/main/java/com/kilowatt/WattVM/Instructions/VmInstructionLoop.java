package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
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
    public void run(WattVM vm, VmFrame<String, Object> frame) {
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
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "LOOP()");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : instructions.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "START_LOOP(" + instructions + ")";
    }
}
