package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.Entities.VmTable;

/**
 * Инструкция ВМ
 */
public interface VmInstruction {
    void run(WattVM vm, VmTable<String, Object> scope);
    void print(int indent);
}
