package com.kilowatt.WattVM.Entities;

/*
Функция, с возможностью бинда
 */
public interface VmBindableFunction {
    void setSelfBind(VmFunctionOwner owner);
    VmFunctionOwner getSelfBind();
}
