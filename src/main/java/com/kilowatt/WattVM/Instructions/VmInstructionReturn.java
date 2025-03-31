package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
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
    private final VmBaseInstructionsBox ret;
    private final VmAddress addr;

    // конструктор
    public VmInstructionReturn(VmBaseInstructionsBox ret, VmAddress addr) {
        this.ret = ret;
        this.addr = addr;
    }

    public void pushResult(WattVM vm, VmFrame<String, Object> scope)  {
        for (VmInstruction i : ret.getInstructionContainer()) {
            i.run(vm, scope);
        }
    }

    public Object getResult(WattVM vm, VmFrame<String, Object> scope)  {
        for (VmInstruction i : ret.getInstructionContainer()) {
            i.run(vm, scope);
        }
        return vm.pop();
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> scope) {
        throw this;
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "RET()");
        VmCodeDumper.dumpLine(indent + 1, "VALUE:");
        for (VmInstruction instruction : ret.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "RETURN_VALUE(" + getRet() + ")";
    }
}
