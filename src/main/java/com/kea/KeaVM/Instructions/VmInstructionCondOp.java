package com.kea.KeaVM.Instructions;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Entities.VmInstance;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

/*
Кодишен VM
 */
@SuppressWarnings("ConstantValue")
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

    public static <T> T cast(Class<T> clazz, Object o) {
        return clazz.cast(o);
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        Object r = vm.pop();
        Object l = vm.pop();
        switch (operator) {
            case "==" -> vm.push(equal(l, r));
            case "!=" -> vm.push(!equal(l, r));
            case "<" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isLessThan(lNumber, rNumber));
                } else {
                    throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            case ">" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isGreaterThan(lNumber, rNumber));
                } else {
                    throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            case "<=" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isLessOrEqual(lNumber, rNumber));
                } else {
                    throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            case ">=" -> {
                if (l instanceof Number lNumber && r instanceof Number rNumber) {
                    vm.push(isGreaterOrEqual(lNumber, rNumber));
                } else {
                    throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                            "Not a number: " + (l instanceof Number ? l : r),
                            "Check types!");
                }
            }
            default -> throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Invalid operator: " + operator,
                    "Available conditional operators: ==, !=, >, >=, <=, <");
        }
    }

    // равны ли два объекта
    public static boolean equal(Object l, Object r) {
        if (l instanceof String && r instanceof String) {
            return ((String)l).equals(((String)r));
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
        else if (l instanceof VmType && r instanceof VmType) {
            return ((VmType)l) == ((VmType)r);
        }
        else if (l instanceof VmInstance && r instanceof VmInstance) {
            return ((VmInstance)l) == ((VmInstance)r);
        }
        else if (l instanceof VmUnit && r instanceof VmUnit) {
            return ((VmUnit)l) == ((VmUnit)r);
        }
        else if (l instanceof Boolean && r instanceof Boolean) {
            return (((boolean)l) == ((boolean)r));
        }
        else if (l instanceof Float && r instanceof Float) {
            return (((float) l) == ((float) r));
        }
        else {
            // throw new RuntimeException("invalid comparables: " + l + ", " + r);
            return false;
        }
    }

    private int compare(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return Double.compare(a.doubleValue(), b.doubleValue());
        } else if (a instanceof Float || b instanceof Float) {
            return Float.compare(a.floatValue(), b.floatValue());
        } else if (a instanceof Long || b instanceof Long) {
            return Long.compare(a.longValue(), b.longValue());
        } else if (a instanceof Integer || b instanceof Integer) {
            return Integer.compare(a.intValue(), b.intValue());
        } else {
            throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Not a number: " + (a != null ? a : b),
                    "Check types!");
        }
    }

    private boolean isGreaterThan(Number a, Number b) {
        return compare(a, b) > 0;
    }

    private boolean isGreaterOrEqual(Number a, Number b) {
        return compare(a, b) >= 0;
    }

    private boolean isLessOrEqual(Number a, Number b) {
        return compare(a, b) <= 0;
    }

    private boolean isLessThan(Number a, Number b) {
        return compare(a, b) < 0;
    }

    @Override
    public String toString() {
        return "CONDITIONAL_OP(" + operator + ")";
    }
}
