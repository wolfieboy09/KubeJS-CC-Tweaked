package com.wolfieboy09.kjscc.peripheral;

import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.List;

public class PeripheralJS extends EventJS {
    private final BlockStatePredicate predicate;
    private final String type;
    private final List<PeripheralMethod> methods;

    public PeripheralJS(BlockStatePredicate state, String type, List<PeripheralMethod> methods) {
        this.predicate = state;
        this.type = type;
        this.methods = methods;
    }

    @HideFromJS
    public boolean test(BlockContainerJS block) {
        return predicate.test(block.getBlockState());
    }

    @HideFromJS
    public String getType() {
        return type;
    }

    @HideFromJS
    public List<PeripheralMethod> getMethods() {
        return methods;
    }

    public PeripheralJS method(String type, DynamicPeripheralJS.PeripheralCallback method) {
        methods.add(new PeripheralMethod(type, method));
        return this;
    }

    public PeripheralJS mainThreadMethod(String type, DynamicPeripheralJS.PeripheralCallback method) {
        methods.add(new PeripheralMethod(type, method, true));
        return this;
    }
}
