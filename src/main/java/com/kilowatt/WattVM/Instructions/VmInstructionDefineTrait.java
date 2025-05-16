package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.Entities.VmTrait;
import com.kilowatt.WattVM.Entities.VmTraitFunction;
import com.kilowatt.WattVM.Entities.VmTable;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Определение юнита
 */
@Getter
@AllArgsConstructor
public class VmInstructionDefineTrait implements VmInstruction {
    // адрес
    private final VmAddress address;
    // трэйт
    private final VmTrait trait;

    @Override
    public void run(WattVM vm, VmTable<String, Object> table)  {
        // дефайн по имени
        vm.getTraitDefinitions().forceSet(address, trait.getName(), trait);
        // дефайн по полному имени
        if (trait.getFullName() != null) vm.getTraitDefinitions().forceSet(address, trait.getFullName(), trait);
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "DEFINE_TRAIT(" + trait.getName() + ", " + trait.getFullName() + ")");
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmTraitFunction traitFn : trait.getFunctions()) {
            if (traitFn.getDefaultImpl() != null) {
                for (VmInstruction instruction : traitFn.getDefaultImpl().getInstructions()) {
                    instruction.print(indent + 2);
                }
            } else {
                VmCodeDumper.dumpLine(indent + 2, "FN("+traitFn.getParamsAmount()+")");
            }
        }
    }

    @Override
    public String toString() {
        return"DEFINE_TRAIT(" + trait.getName() + ", " + trait.getFullName() + ")";
    }
}
