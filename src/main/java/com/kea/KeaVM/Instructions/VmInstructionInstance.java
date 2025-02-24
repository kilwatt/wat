package com.kea.KeaVM.Instructions;

import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Entities.VmType;
import com.kea.KeaVM.KeaVM;
import com.kea.KeaVM.VmAddress;
import com.kea.KeaVM.VmFrame;
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
    public void run(KeaVM vm, VmFrame<String, Object> frame)  {
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
    private int passArgs(KeaVM vm, VmFrame<String, Object> frame)  {
        int size = vm.getStack().size();
        for (VmInstruction instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
        return vm.getStack().size()-size;
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            throw new KeaRuntimeError(addr.getLine(), addr.getFileName(),
                    "Invalid args amount for constructor of class: "
                    + className + "(" + argsAmount + "/" + parameterAmount + " )",
                    "Check arguments amount!");
        }
    }
}
