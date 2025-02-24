package com.kea.KeaVM.Codegen;

import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Boxes.VmInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstruction;
import com.kea.KeaVM.KeaVM;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Stack;

/*
Код для ВМ
 */
@Getter
@Setter
public class KeaVmCode {
    private final Stack<VmInstructionsBox> writing = new Stack<>();

    public KeaVmCode() {
        this.writing.add(new VmBaseInstructionsBox());
    }

    public void writeTo(VmInstructionsBox box) {
        writing.push(box);
    }

    public void endWrite() {
        writing.pop();
    }

    public void visitInstruction(VmInstruction instruction) {
        this.writing.lastElement().visitInstr(instruction);
    }

    public void run(KeaVM vm) {
        if (this.writing.size() != 1) {
            System.out.println("Warning! Codegen has depth more than one scheduled for writing!");
            System.out.println("Send your code with this message to developers. #003::CompilationPhase");
        }
        ((VmBaseInstructionsBox)this.writing.lastElement()).execWithoutPop(vm, vm.getGlobals());
    }
}
