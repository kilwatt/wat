package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Инструкция арифметической операции
 */
@SuppressWarnings({"ClassCanBeRecord", "UnnecessaryToStringCall"})
@Getter
public class VmInstructionBinOp implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // оператор
    private final String operator;

    public VmInstructionBinOp(VmAddress addr, String operator) {
        this.addr = addr;
        this.operator = operator;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        Object r = vm.pop();
        Object l = vm.pop();
        if (r == null || l == null) {
            String nullSide = r == null ? "right" : "left";
            throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "couldn't use " + operator + " operator with null from " + nullSide + " side.",
                    "check types.");
        }
        if (r instanceof String || l instanceof String) {
            switch (operator) {
                case "+" -> vm.push(l.toString() + r.toString());
                case "*" -> {
                    if (r instanceof Number number) {
                        vm.push(l.toString().repeat(number.intValue()));
                    } else {
                        throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                                "couldn't use * operator with string && string.",
                                "check types.");
                    }
                }
                default -> throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                        "couldn't " + operator + " with strings.",
                        "check your code.");
            }
        } else {
            if (l instanceof Number lNumber) {
                if (r instanceof Number rNumber) {
                    switch (operator) {
                        case "+" -> vm.push(add(lNumber, rNumber));
                        case "-" -> vm.push(sub(lNumber, rNumber));
                        case "*" -> vm.push(mul(lNumber, rNumber));
                        case "/" -> {
                            if (rNumber.floatValue() == 0) {
                                throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                                        "can't divide by zero.",
                                        "check your code.");
                            }
                            vm.push(div(lNumber, rNumber));
                        } default -> {

                        }
                    }
                } else {
                    throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                            "couldn't use op: " + operator + ". right is not a number: " + r,
                            "check your code.");
                }
            }
            else {
                throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                        "couldn't use op: " + operator + ". left is not a number: " + l,
                        "check your code.");
            }
        }
    }

    private Number add(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() + b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() + b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() + b.longValue();
        } else {
            return a.intValue() + b.intValue();
        }
    }

    private Number sub(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() - b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() - b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() - b.longValue();
        } else {
            return a.intValue() - b.intValue();
        }
    }

    private Number mul(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() * b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() * b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() * b.longValue();
        } else {
            return a.intValue() * b.intValue();
        }
    }

    private Number div(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() / b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() / b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() / b.longValue();
        } else {
            return a.intValue() / b.intValue();
        }
    }

    @Override
    public String toString() {
        return "DO_BINARY_OP(" + operator + ")";
    }
}
