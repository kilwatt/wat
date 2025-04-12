package com.kilowatt.Compiler.Builtins.Libraries.Std.Math;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

/*
Доп. математические функции
 */
@SuppressWarnings("IfCanBeSwitch")
public class StdMathExt {
    public static Number cos(Number value) {
        if (value instanceof Integer) {
            return (float) Math.cos(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.cos(value.longValue());
        } else if (value instanceof Float) {
            return Math.cos(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.cos(value.doubleValue());
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
            return (float) Math.abs(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.abs(value.longValue());
        } else if (value instanceof Float) {
            return Math.abs(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.abs(value.doubleValue());
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
            return (float) value.intValue();
        } else if (value instanceof Long) {
            return (float) Math.floor(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.floor(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.floor(value.doubleValue());
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
            return (float) Math.sqrt(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.sqrt(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.sqrt(value.floatValue());
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

    public static Number sin(Number value) {
        if (value instanceof Integer) {
            return (float) Math.sin(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.sin(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.sin(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.sin(value.doubleValue());
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

    public static Number tan(Number value) {
        if (value instanceof Integer) {
            return (float) Math.tan(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.tan(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.tan(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.tan(value.doubleValue());
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

    public static Number asin(Number value) {
        if (value instanceof Integer) {
            return (float) Math.asin(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.asin(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.asin(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.asin(value.doubleValue());
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

    public static Number acos(Number value) {
        if (value instanceof Integer) {
            return (float) (Math.acos(value.intValue()));
        } else if (value instanceof Long) {
            return (float) Math.acos(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.acos(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.acos(value.doubleValue());
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

    public static Number atan(Number value) {
        if (value instanceof Integer) {
            return (float) Math.atan(value.intValue());
        } else if (value instanceof Long) {
            return (float) Math.atan(value.longValue());
        } else if (value instanceof Float) {
            return (float) Math.atan(value.floatValue());
        } else if (value instanceof Double) {
            return (float) Math.atan(value.doubleValue());
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
