package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

/*
Ломает текущее выполнение, перед
вызовом этой инструкции в стек помещается значение
для возврата, которое в дальнейшем ловится
инструкцией Store
 */
@Getter
public class VmInstructionReturn extends RuntimeException implements VmInstruction {
    // адресс
    private final VmChunk ret;
    private final VmAddress address;

    // конструктор
    public VmInstructionReturn(VmChunk ret, VmAddress address) {
        this.ret = ret;
        this.address = address;
    }

    public void pushResult(WattVM vm, VmFrame<String, Object> scope)  {
        for (VmInstruction i : ret.getInstructions()) {
            i.run(vm, scope);
        }
    }

    public Object getResult(WattVM vm, VmFrame<String, Object> scope)  {
        for (VmInstruction i : ret.getInstructions()) {
            i.run(vm, scope);
        }
        return vm.pop(address);
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> scope) {
        throw this;
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "RET()");
        VmCodeDumper.dumpLine(indent + 1, "VALUE:");
        for (VmInstruction instruction : ret.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "RETURN_VALUE(" + getRet() + ")";
    }
}
