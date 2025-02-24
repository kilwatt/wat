package com.kea.KeaVM.Instructions;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Entities.VmInstance;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.Entities.VmUnit;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
IF для VM
 */
@SuppressWarnings("ConstantValue")
@Getter
public class VmInstructionIf implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // инструкции условий
    private final VmBaseInstructionsBox conditional = new VmBaseInstructionsBox();
    // инструкции
    private final VmBaseInstructionsBox instructions = new VmBaseInstructionsBox();
    // else
    @Setter
    private VmInstructionIf elseStatement;

    public VmInstructionIf(VmAddress addr) {
        this.addr = addr;
    }

    public static <T> T cast(Class<T> clazz, Object o) {
        return clazz.cast(o);
    }

    @Override
    public void run(KeaVM vm, VmFrame<String, Object> frame) {
        Object val = vm.pop();

    }

    @Override
    public String toString() {
        return "IF_VALUE()";
    }
}
