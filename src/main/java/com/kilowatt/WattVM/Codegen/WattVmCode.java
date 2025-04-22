package com.kilowatt.WattVM.Codegen;

import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.WattVM;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;

/*
Код для ВМ
 */
@Getter
@Setter
public class WattVmCode {
    // стэк боксов для написания
    private final ArrayDeque<VmChunk> writing = new ArrayDeque<>();

    // конструктор
    public WattVmCode() {
        this.writing.add(new VmChunk());
    }

    // запись в бокс
    public void writeTo(VmChunk chunk) {
        writing.push(chunk);
    }

    // конец записи в бокс
    public void endWrite() {
        writing.pop();
    }

    // добавление инструкции
    public void visitInstruction(VmInstruction instruction) {
        this.writing.getFirst().visitInstr(instruction);
    }

    // запуск кода
    public void run(WattVM vm) {
        if (this.writing.size() != 1) {
            System.out.println("Warning! Codegen has depth more than one scheduled for writing!");
            System.out.println("Send your code with this message to developers. #003::CompilationPhase");
        }
        // получаем код
        VmChunk chunk = this.writing.getFirst();
        // запускаем
        chunk.run(vm, vm.getGlobals());
    }

    // вывод кода
    public void print(boolean asFile) {
        // получаем код
        VmChunk chunk = this.writing.getFirst();
        // дампим
        VmCodeDumper.dump(chunk.getInstructions(), asFile);
    }
}
