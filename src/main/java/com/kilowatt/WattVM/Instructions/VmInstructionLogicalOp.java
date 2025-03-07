package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Кодишен VM
 */
@SuppressWarnings("ConstantValue")
@Getter
public class VmInstructionLogicalOp implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // оператор
    private final String operator;

    public VmInstructionLogicalOp(VmAddress addr, String operator) {
        this.addr = addr;
        this.operator = operator;
    }

    public static <T> T cast(Class<T> clazz, Object o) {
        return clazz.cast(o);
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        Object r = vm.pop();
        Object l = vm.pop();
        switch (operator) {
            case "and" -> vm.push((boolean)l && ((boolean)r));
            case "or" -> vm.push((boolean)l || ((boolean)r));
            default -> throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "Invalid operator: " + operator,
                    "Available conditional operators: and, or");
        }
    }

    @Override
    public String toString() {
        return "LOGICAL_OP(" + operator + ")";
    }
}
