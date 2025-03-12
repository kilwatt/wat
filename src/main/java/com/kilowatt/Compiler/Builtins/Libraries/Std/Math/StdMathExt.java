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
            return Math.cos(value.intValue());
        } else if (value instanceof Long) {
            return Math.cos(value.longValue());
        } else if (value instanceof Float) {
            return Math.cos(value.floatValue());
        } else if (value instanceof Double) {
            return Math.cos(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            return Math.sin(value.intValue());
        } else if (value instanceof Long) {
            return Math.sin(value.longValue());
        } else if (value instanceof Float) {
            return Math.sin(value.floatValue());
        } else if (value instanceof Double) {
            return Math.sin(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            return Math.tan(value.intValue());
        } else if (value instanceof Long) {
            return Math.tan(value.longValue());
        } else if (value instanceof Float) {
            return Math.tan(value.floatValue());
        } else if (value instanceof Double) {
            return Math.tan(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            return Math.asin(value.intValue());
        } else if (value instanceof Long) {
            return Math.asin(value.longValue());
        } else if (value instanceof Float) {
            return Math.asin(value.floatValue());
        } else if (value instanceof Double) {
            return Math.asin(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            return Math.acos(value.intValue());
        } else if (value instanceof Long) {
            return Math.acos(value.longValue());
        } else if (value instanceof Float) {
            return Math.acos(value.floatValue());
        } else if (value instanceof Double) {
            return Math.acos(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
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
            return Math.atan(value.intValue());
        } else if (value instanceof Long) {
            return Math.atan(value.longValue());
        } else if (value instanceof Float) {
            return Math.atan(value.floatValue());
        } else if (value instanceof Double) {
            return Math.atan(value.doubleValue());
        } else {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "not a number: " + value,
                    "check your code."
            );
        }
    }
}
