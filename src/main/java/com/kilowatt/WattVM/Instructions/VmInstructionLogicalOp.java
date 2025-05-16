package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Кодишен VM
 */
@Getter
public class VmInstructionLogicalOp implements VmInstruction {
    // адресс
    private final VmAddress address;
    // оператор
    private final String operator;

    public VmInstructionLogicalOp(VmAddress address, String operator) {
        this.address = address;
        this.operator = operator;
    }
    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        Object r = vm.pop(address);
        Object l = vm.pop(address);
        switch (operator) {
            case "and" -> vm.push((boolean)l && ((boolean)r));
            case "or" -> vm.push((boolean)l || ((boolean)r));
            default -> throw new WattRuntimeError(address,
                    "Invalid operator: " + operator,
                    "Available conditional operators: and, or");
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "LOGICAL_OP("+operator+")");
    }

    @Override
    public String toString() {
        return "LOGICAL_OP(" + operator + ")";
    }
}
