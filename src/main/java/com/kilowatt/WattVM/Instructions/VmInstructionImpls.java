package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmTrait;
import com.kilowatt.WattVM.Storage.VmFrame;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;

/*
Проверяет, имплементирует ли трэйт объект.
 */
@AllArgsConstructor
public class VmInstructionImpls implements VmInstruction {
    private final VmAddress address;
    private final String traitName;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> scope) {
        Object instanceObj = vm.pop(address);
        VmTrait trait = vm.getTraitDefinitions().lookup(address, traitName);
        if (instanceObj instanceof VmInstance instance) {
           vm.push(instance.getType().hasTrait(trait));
        } else {
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "couldn't check impls, not a instance: " + instanceObj.getClass().getSimpleName(),
                "check your code."
            );
        }
    }

    @Override
    public void print(int indent) {

    }
}
