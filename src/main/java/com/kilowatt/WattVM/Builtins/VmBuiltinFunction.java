package com.kilowatt.WattVM.Builtins;

import com.kilowatt.WattVM.Entities.VmBindableFunction;
import com.kilowatt.WattVM.Entities.VmFunctionOwner;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;
import lombok.Setter;

/*
Билт ин функция
 */
public abstract class VmBuiltinFunction implements VmBindableFunction {
    // self бинд
    @Getter
    @Setter
    private VmFunctionOwner selfBind;
    // методы для работы с функцией
    public abstract void exec(WattVM vm, VmAddress address, boolean shouldPushResult);
    public abstract int paramsAmount();
    public abstract String getName();
}
