package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
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

    public void pushResult(KeaVM vm, VmFrame<String, Object> scope)  {
        for (VmInstruction i : ret.getVarContainer()) {
            i.run(vm, scope);
        }
    }

    public Object getResult(KeaVM vm, VmFrame<String, Object> scope)  {
        for (VmInstruction i : ret.getVarContainer()) {
            i.run(vm, scope);
        }
        return vm.pop();
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> scope) {
        throw this;
    }

    @Override
    public String toString() {
        return "RETURN_VALUE()";
    }
}
