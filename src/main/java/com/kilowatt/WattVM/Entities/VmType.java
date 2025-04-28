package com.kilowatt.WattVM.Entities;

import com.kilowatt.WattVM.Chunks.VmChunk;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Кастомный тип VM
 */
@Getter
@AllArgsConstructor
public class VmType {
    // имя, полное имя
    private final String name;
    private final String fullName;
    // конструктор
    private final ArrayList<String> constructor;
    // трэйты
    private final ArrayList<String> traits;
    // чанк инициализации
    private final VmChunk body;

    @Override
    public String toString() {
        return "VmType(" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", constructor=" + constructor +
                ", traits=" + traits +
                ", body=" + body +
                ')';
    }

    // есть ли трэйт
    public boolean hasTrait(VmTrait trait) {
        return traits.contains(trait.getName()) || traits.contains(trait.getFullName());
    }
}
