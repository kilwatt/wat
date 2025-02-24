package com.kea.KeaVM.Entities;

import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

/*
Кастомный тип VM
 */
@Getter
public class VmUnit {
    private final String name;
    private final VmFrame<String, Object> fields;

    public VmUnit(String name, VmFrame<String, Object> fields) {
        this.name = name;
        this.fields = fields;
    }
}
