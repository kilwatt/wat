package com.kilowatt.WattVM.Reflection;

import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Entities.VmTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Инфа о вызове
 */
@Getter
@AllArgsConstructor
public class VmCallInfo {
    // адрес, откуда произошёл вызов
    private final VmAddress address;
    // имя функции
    private final String name;
    // фрэйм вызова
    private final VmTable<String, Object> table;

    // в строку
    @Override
    public String toString() {
        return name + " call at " + address.getFileName() + ":" + address.getLine() + ":" + address.getColumn();
    }
}
