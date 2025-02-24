package com.kea.KeaVM.Instructions;

import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmFrame;

/**
 * Инструкция ВМ
 */
public interface VmInstruction {
    void run(KeaVM vm, VmFrame<String, Object> scope);
}
