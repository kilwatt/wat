package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.Chunks.VmChunk;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Кастомный трэйт VM
 */
@Getter
@AllArgsConstructor
public class VmTrait {
    private final String name;
    private final String fullName;
    private final ArrayList<VmTraitFunction> functions;

    @Override
    public String toString() {
        return "VmTrait(" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", functions=" + functions +
                ')';
    }
}
