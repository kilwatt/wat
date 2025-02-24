package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.Entities.VmFunction;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Инструкция создания замыкания
 */
@Getter
public class VmInstructionMakeClosure implements VmInstruction {
    // аддресс
    private final VmAddress addr;
    // имя функции
    private final String name;

    // конструктор
    public VmInstructionMakeClosure(VmAddress addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    // конструктор
    public VmInstructionMakeClosure(VmAddress addr) {
        this.addr = addr;
        this.name = null;
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> scope) {
        VmFunction fn;
        if (name == null) {
            fn = (VmFunction) vm.pop();
        }
        else {
            fn = (VmFunction) scope.lookup(addr, name);
        }
        fn.setClosure(scope.copy());
    }

    @Override
    public String toString() {
        return "MAKE_CLOSURE(" + name + ")";
    }
}
