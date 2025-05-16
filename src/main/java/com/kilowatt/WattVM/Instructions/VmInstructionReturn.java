package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmReturnValue;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Ломает текущее выполнение, перед
вызовом этой инструкции в стек помещается значение
для возврата, которое в дальнейшем ловится
инструкцией Store
 */
@Getter
public class VmInstructionReturn implements VmInstruction {
    // адресс
    private final VmChunk value;
    private final VmAddress address;

    // конструктор
    public VmInstructionReturn(VmChunk value, VmAddress address) {
        this.value = value;
        this.address = address;
    }

    public Object getResult(WattVM vm, VmTable<String, Object> scope)  {
        return value.runAndGet(vm, scope, address);
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> scope) {
        throw new VmReturnValue(getResult(vm, scope));
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "RET()");
        VmCodeDumper.dumpLine(indent + 1, "VALUE:");
        for (VmInstruction instruction : value.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "RETURN_VALUE(" + value + ")";
    }
}
