package com.kilowatt.WattVM.Entities;

/*
Владелец функции
 */
public interface VmFunctionOwner {
    VmTable<String, Object> getFields();
}
