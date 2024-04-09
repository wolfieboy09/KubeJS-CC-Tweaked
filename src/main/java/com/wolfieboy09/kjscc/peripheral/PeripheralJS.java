package com.wolfieboy09.kjscc.peripheral;

import dev.latvian.mods.kubejs.bindings.event.PlayerEvents;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PeripheralJS {
    public static final EventGroup GROUP = EventGroup.of("ComputerCraftEvents");


    private final BlockStatePredicate predicate;
    private final String type;
    private final List<PeripheralMethod> methods;

    public PeripheralJS(BlockStatePredicate state, String type, List<PeripheralMethod> methods) {
        this.predicate = state;
        this.type = type;
        this.methods = methods;
    }

    @HideFromJS
    public boolean test(BlockState state) {
        return predicate.test(state);
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
