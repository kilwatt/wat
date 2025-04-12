package com.kilowatt.Compiler.Builtins.Libraries.Std.Math;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

/*
Математические функции
 */
@SuppressWarnings("IfCanBeSwitch")
public class StdMath {
    public static Number ceil(Number value) {
        if (value instanceof Integer) {
            return value.intValue();
        } else if (value instanceof Long) {
            return value.longValue();
        } else if (value instanceof Float) {
            return Math.ceil(value.floatValue());
        } else if (value instanceof Double) {
            return Math.ceil(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number abs(Number value) {
        if (value instanceof Integer) {
            return Math.abs(value.intValue());
        } else if (value instanceof Long) {
            return Math.abs(value.longValue());
        } else if (value instanceof Float) {
            return Math.abs(value.floatValue());
        } else if (value instanceof Double) {
            return Math.abs(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number floor(Number value) {
        if (value instanceof Integer) {
            return value.intValue();
        } else if (value instanceof Long) {
            return Math.floor(value.longValue());
        } else if (value instanceof Float) {
            return Math.floor(value.floatValue());
        } else if (value instanceof Double) {
            return Math.floor(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number sqrt(Number value) {
        if (value instanceof Integer) {
            return Math.sqrt(value.intValue());
        } else if (value instanceof Long) {
            return Math.sqrt(value.longValue());
        } else if (value instanceof Float) {
            return Math.sqrt(value.floatValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number cbrt(Number value) {
        if (value instanceof Integer) {
            return Math.cbrt(value.intValue());
        } else if (value instanceof Long) {
            return Math.cbrt(value.longValue());
        } else if (value instanceof Float) {
            return Math.cbrt(value.floatValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number pow(Number value, Number power) {
        if (value instanceof Integer) {
            if (power instanceof Integer) {
                return Math.pow(value.intValue(), power.intValue());
            } else if (power instanceof Long) {
                return Math.pow(value.intValue(), power.longValue());
            } else if (power instanceof Float) {
                return Math.pow(value.intValue(), power.floatValue());
            } else {
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                throw new WattRuntimeError(
                        address.getLine(),
                        address.getFileName(),
                        "not a number: " + value,
                        "check your code."
                );
            }
        } else if (value instanceof Long) {
            if (power instanceof Integer) {
                return Math.pow(value.longValue(), power.intValue());
            } else if (power instanceof Long) {
                return Math.pow(value.longValue(), power.longValue());
            } else if (power instanceof Float) {
                return Math.pow(value.longValue(), power.floatValue());
            } else {
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                throw new WattRuntimeError(
                        address.getLine(),
                        address.getFileName(),
                        "not a number: " + value,
                        "check your code."
                );
            }
        } else if (value instanceof Float) {
            if (power instanceof Integer) {
                return Math.pow(value.floatValue(), power.intValue());
            } else if (power instanceof Long) {
                return Math.pow(value.floatValue(), power.longValue());
            } else if (power instanceof Float) {
                return Math.pow(value.floatValue(), power.floatValue());
            } else {
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                throw new WattRuntimeError(
                        address.getLine(),
                        address.getFileName(),
                        "not a number: " + value,
                        "check your code."
                );
            }
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number log(Number value) {
        if (value instanceof Integer) {
            return Math.log(value.intValue());
        } else if (value instanceof Long) {
            return Math.log(value.longValue());
        } else if (value instanceof Float) {
            return Math.log(value.floatValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }

    public static Number log10(Number value) {
        if (value instanceof Integer) {
            return Math.log10(value.intValue());
        } else if (value instanceof Long) {
            return Math.log10(value.longValue());
        } else if (value instanceof Float) {
            return Math.log10(value.floatValue());
        } else {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }
}
