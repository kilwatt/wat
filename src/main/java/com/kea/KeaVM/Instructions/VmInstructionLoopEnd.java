package com.kea.KeaVM.Instructions;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
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
    public void run(KeaVM vm, VmFrame<String, Object> scope) {
        throw this;
    }

    @Override
    public String toString() {
        return "LOOP_END(CURRENT_ITERATION:"+ currentIteration +")";
    }
}
