package com.kilowatt.Compiler.Builtins.Libraries.Std;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.Entities.VmType;
import com.kilowatt.WattVM.Entities.VmUnit;
import com.kilowatt.WattVM.Instructions.VmInstructionCall;
import com.kilowatt.WattVM.Instructions.VmInstructionLoad;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import com.kilowatt.WattVM.VmAddress;

/*
Стд -> Рефлексия
 */
public class StdReflection {
    public VmInstance instance(VmType type, WattList args) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // аргументы
        for (Object o : args.getList()) {
            WattCompiler.vm.push(o);
        }
        // инстанс
        return new VmInstance(WattCompiler.vm, type, new VmAddress("__refl__", -1, -1, "__refl__"));
    }

    public VmType lookup_type(String name) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // возвращаем тип
        return WattCompiler.vm.getTypeDefinitions().lookup(address, name);
    }

    public VmUnit lookup_unit(String name) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // возвращаем тип
        return WattCompiler.vm.getUnitDefinitions().lookup(address, name);
    }

    public Object call_function(Object object, String name, WattList args) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // помещаем объект
        WattCompiler.vm.push(object);
        // аргументы
        VmChunk argsChunk = new VmChunk();
        for (Object arg: args.getList()) {
            argsChunk.visitInstr(
                new VmInstructionPush(
                    address,
                    arg
                )
            );
        }
        // вызов
        new VmInstructionCall(
            address,
            name,
            argsChunk,
            true,
            true
        ).run(WattCompiler.vm, WattCompiler.vm.getGlobals());
        // возврат значения
        return WattCompiler.vm.pop(address);
    }

    public Object get_field(Object object, String name) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // помещаем объект
        WattCompiler.vm.push(object);
        // вызов
        new VmInstructionLoad(
            address,
            name,
            true,
            true
        ).run(WattCompiler.vm, WattCompiler.vm.getGlobals());
        // возврат значения
        return WattCompiler.vm.pop(address);
    }
}
