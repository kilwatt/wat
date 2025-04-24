package com.kilowatt.WattVM.Entities;

import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.WattVM.Storage.VmFrame;
import lombok.Getter;

/*
Инстанс класса ВМ
 */
@Getter
public class VmInstance implements VmFunctionOwner {
    // класс
    private final VmType type;
    // скоуп
    private final VmFrame<String, Object> fields = new VmFrame<>();
    // адрес
    private final VmAddress address;

    // конструктор
    public VmInstance(WattVM vm, VmType type, VmAddress address)  {
        // данные
        this.type = type;
        this.address = address;
        // конструктор
        for (int i = type.getConstructor().size()-1; i >= 0; i--) {
            Object arg = vm.pop(address);
            fields.define(address, type.getConstructor().get(i), arg);
        }
        // установка филдов
        fields.setRoot(vm.getGlobals());
        type.getBody().run(vm, fields);
        // проверка трэйтов
        checkTraits(vm);
        // бинды функций
        bindFunctionsToInstance();
        // init функция
        if (fields.has("init")) {
            call(address, "init", vm, false);
        }
    }

    /**
     * Проверка трэйтов.
     * Добавление дефолтных реализаций, если
     * тип не реализует трэйт
     * @param vm - ВМ
     */
    void checkTraits(WattVM vm) {
        for (String traitName : getType().getTraits()) {
            VmTrait trait = vm.getTraitDefinitions().lookup(address, traitName);
            // проходимся по функциям трэйта
            for (VmTraitFunction traitFn : trait.getFunctions()) {
                // если есть имплементация
                if (fields.has(traitFn.getName())) {
                    // проверяем имплементацию
                    Object impl = fields.lookup(address, traitFn.getName());
                    if (impl instanceof VmFunction fnImpl) {
                        if (fnImpl.getParams().size() != traitFn.getParamsAmount()) {

                        }
                    }
                }
                // если нет имлпементации
                else {
                    // если есть дефолтная имплементация
                    if (traitFn.getDefaultImpl() != null) {
                        traitFn.getDefaultImpl().run(vm, fields);
                    }
                    // если нет дефолтной имплементации
                    else {
                        throw new WattRuntimeError(
                                address.getLine(),
                                address.getFileName(),
                                "type " + type.getName() + " impls trait " + traitName + ", but doesn't impl fn " +
                                        traitFn.getName() + " (" + traitFn.getParamsAmount() + ")",
                                "you can create default impl in trait."
                        );
                    }
                }
            }
        }
    }

    /**
     Бинды копий функций типа к экземпляру типа
     */
    private void bindFunctionsToInstance() {
        // Фильтруются функции, которые не привязаны
        // к конкретному юниту или экземпляру типа. В последствии
        // они привязываются к этому экземпляру типа.
        fields.getValues().values().stream()
            .filter(field -> field instanceof VmFunction fn)
            .map(field -> (VmFunction) field)
            .filter(fn -> fn.getSelfBind() == null)
            .forEach(fn -> fn.setSelfBind(this));
    }

    /**
     * Вызов функции объекта
     * @param name - имя функции
     * @param vm - ВМ
     */
    public void call(VmAddress address, String name, WattVM vm, boolean shouldPushResult)  {
        // ищем функцию
        Object val = getFields().lookup(address, name);
        // проверяем, функция ли
        if (val instanceof VmFunction fn) {
            fn.exec(vm, shouldPushResult);
        } else {
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "couldn't call: " + name + ", not a fn.",
                "check your code"
            );
        }
    }

    // в строку

    @Override
    public String toString() {
        return "VmInstance(" +
                "type=" + type.getName() +
                ", fields=" + fields.getValues().keySet() +
                ", address=" + address +
                ')';
    }

    // получение локального скоупа
    @Override
    public VmFrame<String, Object> getLocalScope() {
        return fields;
    }
}
