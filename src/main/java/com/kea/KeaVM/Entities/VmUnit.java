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
    private final ArrayList<String> constructor;
    private final String fullName;
    private final VmFrame<String, Object> fields;

    public VmUnit(String name, String fullName, ArrayList<String> constructor, VmFrame<String, Object> fields) {
        this.name = name;
        this.fullName = fullName;
        this.constructor = constructor;
        this.fields = fields;
    }
}
