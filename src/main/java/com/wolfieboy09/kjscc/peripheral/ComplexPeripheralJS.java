package com.wolfieboy09.kjscc.peripheral;

import com.wolfieboy09.kjscc.PeripheralRegisterEvent;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ComplexPeripheralJS extends PeripheralJS {
    private final PeripheralRegisterEvent.DynamicPredicate dynamicPredicate;

    public ComplexPeripheralJS (PeripheralRegisterEvent.DynamicPredicate predicate, String type, List<PeripheralMethod> methods) {
        super(null, type, methods);
        this.dynamicPredicate = predicate;
    }

    @HideFromJS
    @Override
    public boolean test(@NotNull BlockContainerJS block) {
        return dynamicPredicate.call(block);
    }
}
