package com.kilowatt.WattVM.Instructions;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Ломает текущее исполнение цикла или переходит на следующую итерацию
 */
@SuppressWarnings("UnnecessaryToStringCall")
@Getter
public class VmInstructionLoopEnd extends RuntimeException implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // только текущую итерацию
    private final boolean currentIteration;

    public VmInstructionLoopEnd(VmAddress addr, boolean currentIteration) {
        this.addr = addr;
        this.currentIteration = currentIteration;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> scope) {
        throw this;
    }

    @Override
    public String toString() {
        return "LOOP_END(CURRENT_ITERATION:"+ currentIteration +")";
    }
}
