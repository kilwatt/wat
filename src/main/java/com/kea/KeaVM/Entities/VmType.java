package com.kea.KeaVM.Entities;

import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import lombok.Getter;

import java.util.ArrayList;

/*
Кастомный тип VM
 */
@Getter
public class VmType {
    private final String name;
    private final ArrayList<String> constructor;
    private final VmBaseInstructionsBox body;

    public VmType(String name,  ArrayList<String> constructor, VmBaseInstructionsBox body) {
        this.name = name;
        this.constructor = constructor;
        this.body = body;
    }

    @Override
    public String toString() {
        return "VmType(" +
                "name='" + name + '\'' +
                ", constructor=" + constructor +
                ", body=" + body +
                ')';
    }
}
