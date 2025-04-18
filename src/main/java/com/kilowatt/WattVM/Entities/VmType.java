package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Кастомный тип VM
 */
@Getter
@AllArgsConstructor
public class VmType {
    private final String name;
    private final String fullName;
    private final ArrayList<String> constructor;
    private final VmBaseInstructionsBox body;

    @Override
    public String toString() {
        return "VmType(" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", constructor=" + constructor +
                ", body=" + body +
                ')';
    }
}
