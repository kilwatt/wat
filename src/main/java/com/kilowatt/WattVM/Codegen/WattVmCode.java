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
    // стэк боксов для написания
    private final Stack<VmInstructionsBox> writing = new Stack<>();

    // конструктор
    public WattVmCode() {
        this.writing.add(new VmBaseInstructionsBox());
    }

    // запись в бокс
    public void writeTo(VmInstructionsBox box) {
        writing.push(box);
    }

    // конец записи в бокс
    public void endWrite() {
        writing.pop();
    }

    // добавление инструкции
    public void visitInstruction(VmInstruction instruction) {
        this.writing.lastElement().visitInstr(instruction);
    }

    // запуск кода
    public void run(WattVM vm) {
        if (this.writing.size() != 1) {
            System.out.println("Warning! Codegen has depth more than one scheduled for writing!");
            System.out.println("Send your code with this message to developers. #003::CompilationPhase");
        }
        // получаем код
        VmBaseInstructionsBox box = ((VmBaseInstructionsBox)this.writing.lastElement());
        // запускаем
        box.run(vm, vm.getGlobals());
    }

    // вывод кода
    public void print(boolean asFile) {
        // получаем код
        VmBaseInstructionsBox box = ((VmBaseInstructionsBox)this.writing.lastElement());
        // дампим
        VmCodeDumper.dump(box.getInstructionContainer(), asFile);
    }
}
