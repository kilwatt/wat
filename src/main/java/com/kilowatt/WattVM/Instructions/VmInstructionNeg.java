package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Инструкция для создания отрицательного числа
 */

@Getter
@AllArgsConstructor
public class VmInstructionNeg implements VmInstruction {
    // адрес
    private final VmAddress address;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> scope) {
        // получаем значение
        Object value = vm.pop(address);
        // создаём негативное число
        switch (value) {
            case Float f -> vm.push(-f);
            case Integer i -> vm.push(-i);
            case Long l -> vm.push(-l);
            case null, default -> throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check for types."
            );
        }
    }

    @Override
    public String toString() {
        return "NEG()";
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "NEG()");
    }
}
