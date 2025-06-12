package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.*;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.Getter;

/*
Инструкция арифметической операции
 */
@SuppressWarnings({"ClassCanBeRecord"})
@Getter
public class VmInstructionBinOp implements VmInstruction {
    // адресс
    private final VmAddress address;
    // оператор
    private final String operator;

    public VmInstructionBinOp(VmAddress address, String operator) {
        this.address = address;
        this.operator = operator;
    }

    @Override
    public void run(WattVM vm, VmTable<String, Object> table) {
        Object r = vm.pop(address);
        Object l = vm.pop(address);
        if (r instanceof String || l instanceof String) {
            switch (operator) {
                case "+" -> vm.push(l.toString() + r.toString());
                case "*" -> {
                    if (r instanceof Number number) {
                        vm.push(l.toString().repeat(number.intValue()));
                    } else {
                        throw new WattRuntimeError(address,
                                "couldn't use * operator with string && string.",
                                "check types.");
                    }
                }
                default -> throw new WattRuntimeError(address,
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
                                throw new WattRuntimeError(address,
                                        "can't divide by zero.",
                                        "check your code.");
                            }
                            vm.push(div(lNumber, rNumber));
                        } case "%" -> {
                            if (rNumber.floatValue() == 0) {
                                throw new WattRuntimeError(address,
                                        "can't divide by zero.",
                                        "check your code.");
                            }
                            vm.push(rem(lNumber, rNumber));
                        } default -> throw new WattRuntimeError(address,
                                "invalid binary op: " + operator,
                                "available binary operators: +, -, *, /, %");
                    }
                } else {
                    throw new WattRuntimeError(address,
                            "couldn't use op " + operator + ", right is not a number: " + r,
                            "check your code.");
                }
            }
            else {
                throw new WattRuntimeError(address,
                        "couldn't use op " + operator + ", left is not a number: " + l,
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
            int primitiveA = a.intValue();
            int primitiveB = b.intValue();
            long result = (long) primitiveA + (long) primitiveB;
            if (result > Integer.MAX_VALUE) {
                return result;
            } else {
                return primitiveA + primitiveB;
            }
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
            int primitiveA = a.intValue();
            int primitiveB = b.intValue();
            long result = (long) primitiveA - (long) primitiveB;
            if (result > Integer.MAX_VALUE) {
                return result;
            } else {
                return primitiveA - primitiveB;
            }
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
            int primitiveA = a.intValue();
            int primitiveB = b.intValue();
            long result = (long) primitiveA * (long) primitiveB;
            if (result > Integer.MAX_VALUE) {
                return result;
            } else {
                return primitiveA * primitiveB;
            }
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
            int primitiveA = a.intValue();
            int primitiveB = b.intValue();
            long result = (long) primitiveA / (long) primitiveB;
            if (result > Integer.MAX_VALUE) {
                return result;
            } else {
                return primitiveA / primitiveB;
            }
        }
    }

    private Number rem(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() % b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() % b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() % b.longValue();
        } else {
            return a.intValue() % b.intValue();
        }
    }

    @Override
    public String toString() {
        return "DO_BINARY_OP(" + operator + ")";
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "BIN_OP("+operator+")");
    }
}
