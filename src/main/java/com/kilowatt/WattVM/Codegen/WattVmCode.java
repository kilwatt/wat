package com.kilowatt.WattVM.Codegen;

import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Boxes.VmInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.WattVM;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/*
Код для ВМ
 */
@Getter
@Setter
public class WattVmCode {
    private final Stack<VmInstructionsBox> writing = new Stack<>();

    public WattVmCode() {
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

    public void run(WattVM vm) {
        if (this.writing.size() != 1) {
            System.out.println("Warning! Codegen has depth more than one scheduled for writing!");
            System.out.println("Send your code with this message to developers. #003::CompilationPhase");
        }
        ((VmBaseInstructionsBox)this.writing.lastElement()).run(vm, vm.getGlobals());
    }

    public void print() {
        VmBaseInstructionsBox box = ((VmBaseInstructionsBox)this.writing.lastElement());
        VmCodeDumper.dump(box.getInstructionContainer());
    }
}
