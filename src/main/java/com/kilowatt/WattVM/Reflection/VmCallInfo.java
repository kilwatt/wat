package com.kilowatt.WattVM.Reflection;

import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Инфа о вызове
 */
@Getter
@AllArgsConstructor
public class VmCallInfo {
    private final VmAddress address;
    private final VmFrame<String, Object> frame;
}
