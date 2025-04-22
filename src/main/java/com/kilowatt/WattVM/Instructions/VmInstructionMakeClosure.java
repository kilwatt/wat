package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

/*
Инструкция создания замыкания
 */
@Getter
public class VmInstructionMakeClosure implements VmInstruction {
    // аддресс
    private final VmAddress address;
    // имя функции
    private final String name;

    // конструктор
    public VmInstructionMakeClosure(VmAddress address, String name) {
        this.address = address;
        this.name = name;
    }

    // конструктор
    public VmInstructionMakeClosure(VmAddress address) {
        this.address = address;
        this.name = null;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> scope) {
        VmFunction fn;
        if (name == null) {
            fn = (VmFunction) vm.pop(address);
        }
        else {
            fn = (VmFunction) scope.lookup(address, name);
        }
        fn.setClosure(scope);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "MAKE_CLOSURE(FN:" + name + ")");
    }

    @Override
    public String toString() {
        return "MAKE_CLOSURE(" + name + ")";
    }
}
