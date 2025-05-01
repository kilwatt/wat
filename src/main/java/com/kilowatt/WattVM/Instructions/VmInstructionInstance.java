package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Codegen.VmCodeDumper;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Помещение инстанса типа в стек VM
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
@AllArgsConstructor
public class VmInstructionInstance implements VmInstruction {
    // адресс
    private final VmAddress address;
    // имя класса
    private final String typeName;
    // аргументы конструктора
    private final VmChunk args;

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // конструктор
        int amount = passArgs(vm, frame);
        VmType clazz = vm.getTypeDefinitions().lookup(address, typeName);
        checkArgs(clazz.getConstructor().size(), amount);
        vm.push(new VmInstance(vm, clazz, address));
    }

    @Override
    public void print(int indent) {
        VmCodeDumper.dumpLine(indent, "INSTANCE("+ typeName +")");
        VmCodeDumper.dumpLine(indent + 1, "CONSTRUCTOR:");
        for (VmInstruction instruction : args.getInstructions()) {
            instruction.print(indent + 2);
        }
    }

    @Override
    public String toString() {
        return "NEW_INSTANCE(" + typeName + "," + args.getInstructions().size() + ")";
    }

    // передача аргументов
    private int passArgs(WattVM vm, VmFrame<String, Object> frame)  {
        int size = vm.getStack().size();
        args.run(vm, frame);
        return vm.getStack().size()-size;
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            throw new WattRuntimeError(address.getLine(), address.getFileName(),
                    "invalid constructor args to create instance: "
                    + typeName + "(" + argsAmount + "/" + parameterAmount + ")",
                    "check args amount.");
        }
    }
}
