package com.kilowatt.WattVM.Instructions;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmLoopBreak;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Ломает текущее исполнение цикла или переходит на следующую итерацию
 */
@Getter
public class VmInstructionLoopEnd extends RuntimeException implements VmInstruction {
    // адресс
    private final VmAddress address;
    // только текущую итерацию
    private final boolean currentIteration;

    public VmInstructionLoopEnd(VmAddress address, boolean currentIteration) {
        this.address = address;
        this.currentIteration = currentIteration;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> scope) {
        throw new VmLoopBreak(currentIteration);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "LOOP_END(CURRENT_ITERATION:" + currentIteration + ")");
    }

    @Override
    public String toString() {
        return "LOOP_END(CURRENT_ITERATION:"+ currentIteration +")";
    }
}
