package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.Storage.VmFrame;

/*
Владелец функции
 */
public interface VmFunctionOwner {
    VmFrame<String, Object> getLocalScope();
}
