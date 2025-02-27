package com.kea.KeaVM.Entities;

import com.kea.KeaVM.VmFrame;

/*
Владелец функции
 */
public interface VmFunctionOwner {
    VmFrame<String, Object> getLocalScope();
}
