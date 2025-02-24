package com.kea.KeaVM.Codegen;

import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
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
    private final Stack<VmBaseInstructionsBox> writing = new Stack<>();
    private final KeaVM vm = new KeaVM();

    public KeaVmCode() {
        this.writing.add(new VmBaseInstructionsBox());
    }

    public void visitInstruction(VmInstruction instruction) {
        this.writing.firstElement().visitInstr(instruction);
    }

    public void run() {
        if (this.writing.size() != 1) {
            System.out.println("Warning! Codegen has depth more than one!");
            System.out.println("Send your code with this message to developers. #003::CompilationPhase");
        }
        this.writing.firstElement().exec(vm, vm.getGlobals());
    }
}
