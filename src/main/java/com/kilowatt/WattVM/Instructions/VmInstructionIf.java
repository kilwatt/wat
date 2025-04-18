package com.kilowatt.WattVM.Instructions;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;
import lombok.Setter;

/*
IF для VM
 */
@Getter
public class VmInstructionIf implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // инструкции
    @Setter
    private VmBaseInstructionsBox conditions = new VmBaseInstructionsBox();
    private final VmBaseInstructionsBox body = new VmBaseInstructionsBox();
    // else
    @Setter
    private VmInstructionIf elseInstruction;

    public VmInstructionIf(VmAddress addr) {
        this.addr = addr;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame) {
        // создаём новый фрэйм
        VmFrame<String, Object> ifFrame = new VmFrame<>();
        ifFrame.setRoot(frame);
        // выполняем инструкции условий
        Object val = conditions.runAndGet(vm, ifFrame);
        // проверяем условия
        if (((Boolean) val)) {
            // выполняем тело
            body.run(vm, ifFrame);
        }
        // в ином случае
        else {
            if (elseInstruction != null) {
                elseInstruction.run(vm, frame);
            }
        }
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "IF():");
        VmCodeDumper.dumpLine(indent + 1, "CONDITIONS:");
        for (VmInstruction instruction : conditions.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
        VmCodeDumper.dumpLine(indent + 1, "BODY:");
        for (VmInstruction instruction : body.getInstructionContainer()) {
            instruction.print(indent + 2);
        }
        if (elseInstruction != null) {
            VmCodeDumper.dumpLine(indent, "ELSE():");
            elseInstruction.print(indent);
        }
    }

    @Override
    public String toString() {
        return "IF_VALUE(conditions: " + conditions + ", body: " + body + ", else: " + elseInstruction + ")";
    }
}
