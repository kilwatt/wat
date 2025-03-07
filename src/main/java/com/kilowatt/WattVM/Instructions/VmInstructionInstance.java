package com.kilowatt.WattVM.Instructions;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.VmFrame;
import lombok.Getter;

/*
Помещение инстанса типа в стек VM
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class VmInstructionInstance implements VmInstruction {
    // адресс
    private final VmAddress addr;
    // имя класса
    private final String className;
    // аргументы конструктора
    private final VmBaseInstructionsBox args;

    // конструктор
    public VmInstructionInstance(VmAddress addr, String className, VmBaseInstructionsBox args) {
        this.addr = addr;
        this.className = className;
        this.args = args;
    }

    @Override
    public void run(WattVM vm, VmFrame<String, Object> frame)  {
        // конструктор
        int amount = passArgs(vm, frame);
        VmType clazz = vm.getTypeDefinitions().lookup(addr, className);
        checkArgs(clazz.getConstructor().size(), amount);
        vm.push(new VmInstance(vm, clazz, addr));
    }

    @Override
    public String toString() {
        return "NEW_INSTANCE(" + className + "," + args.getVarContainer().size() + ")";
    }

    // передача аргументов
    private int passArgs(WattVM vm, VmFrame<String, Object> frame)  {
        int size = vm.getStack().size();
        for (VmInstruction instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
        return vm.getStack().size()-size;
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            throw new WattRuntimeError(addr.getLine(), addr.getFileName(),
                    "Invalid args amount for constructor of class: "
                    + className + "(" + argsAmount + "/" + parameterAmount + " )",
                    "Check arguments amount!");
        }
    }
}
