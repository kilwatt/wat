package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.Storage.VmFrame;

/**
 * Инструкция ВМ
 */
public interface VmInstruction {
    void run(WattVM vm, VmFrame<String, Object> scope);
    void print(int indent);
}
