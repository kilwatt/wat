package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Кодишен VM
 */
@SuppressWarnings({"ConstantValue", "ClassCanBeRecord"})
@Getter
public class VmInstructionCondOp implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // оператор
    private final String operator;

    public VmInstructionCondOp(VmAddress addr, String operator) {
        this.addr = addr;
        this.operator = operator;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        Object r = vm.pop();
        Object l = vm.pop();
        switch (operator) {
            case "==" -> vm.push(equal(addr, l, r));
            case "!=" -> vm.push(!equal(addr, l, r));
            case "<" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isLessThan(lNumber, rNumber));
                } else {
                    throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            case ">" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isGreaterThan(lNumber, rNumber));
                } else {
                    throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            case "<=" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isLessOrEqual(lNumber, rNumber));
                } else {
                    throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            case ">=" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isGreaterOrEqual(lNumber, rNumber));
                } else {
                    throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            default -> throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "Invalid operator: " + operator,
                    "Available conditional operators: ==, !=, >, >=, <=, <");
        }
    }

    // равны ли два объекта
    public static boolean equal(VmAddress addr, Object l, Object r) {
        if (l instanceof String left && r instanceof String right) {
            return left.equals(right);
        }
        else if (l == null && r != null) {
            return false;
        }
        else if (l != null && r == null) {
            return false;
        }
        else if (l == null && r == null) {
            return true;
        }
        else if (l instanceof VmType left && r instanceof VmType right) {
            return left == right;
        }
        else if (l instanceof VmInstance left && r instanceof VmInstance right) {
            return left == right;
        }
        else if (l instanceof VmUnit left && r instanceof VmUnit right) {
            return left == right;
        }
        else if (l instanceof Boolean left && r instanceof Boolean right) {
            return left == right;
        }
        else if (l instanceof Number a && r instanceof Number b) {
            return compare(addr, a, b) == 0;
        }
        else {
            return false;
        }
    }

    private static int compare(VmAddress addr, Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return Double.compare(a.doubleValue(), b.doubleValue());
        } else if (a instanceof Float || b instanceof Float) {
            return Float.compare(a.floatValue(), b.floatValue());
        } else if (a instanceof Long || b instanceof Long) {
            return Long.compare(a.longValue(), b.longValue());
        } else if (a instanceof Integer || b instanceof Integer) {
            return Integer.compare(a.intValue(), b.intValue());
        } else {
            throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "Not a number: " + (a != null ? a : b),
                    "Check types!");
        }
    }

    private boolean isGreaterThan(Number a, Number b) {
        return compare(addr, a, b) > 0;
    }

    private boolean isGreaterOrEqual(Number a, Number b) {
        return compare(addr, a, b) >= 0;
    }

    private boolean isLessOrEqual(Number a, Number b) {
        return compare(addr, a, b) <= 0;
    }

    private boolean isLessThan(Number a, Number b) {
        return compare(addr, a, b) < 0;
    }

    @Override
    public String toString() {
        return "CONDITIONAL_OP(" + operator + ")";
    }
}
