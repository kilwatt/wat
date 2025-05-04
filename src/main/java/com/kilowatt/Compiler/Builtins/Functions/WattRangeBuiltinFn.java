package com.kilowatt.Compiler.Builtins.Functions;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattIterator;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Builtins.VmBuiltinFunction;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;

import java.util.ArrayList;

/*
Функция для создания рэнджа из чисел
 */
public class WattRangeBuiltinFn implements VmBuiltinFunction {
    @Override
    public void exec(WattVM vm, VmAddress address, boolean shouldPushResult) {
        // получаем объекты из стека
        Object second = vm.pop(address);
        Object first = vm.pop(address);
        // проверяем
        if (first instanceof Integer left &&
            second instanceof Integer right) {
            // генерим итератор
            ArrayList<Integer> range = new ArrayList<>();
            // в зависимости от его направления
            if (left < right) {
                for (int i = left; i < right; i++) {
                    range.add(i);
                }
            } else {
                for (int i = left - 1; i >= right; i--) {
                    range.add(i);
                }
            }
            // пушим
            if (shouldPushResult) {
                vm.push(new WattIterator<>(range.iterator()));
            }
        } else {
            // ошибка
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "couldn't create range with (" + first + "," + second + ")",
                "you can create range with two ints."
            );
        }
    }

    @Override
    public int paramsAmount() {
        return 2;
    }

    @Override
    public String getName() {
        return "rng";
    }
}
