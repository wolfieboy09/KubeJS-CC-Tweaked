package com.wolfieboy09.kjscc;

import com.wolfieboy09.kjscc.events.ComputerCraftEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ComputerCraftPlugin extends KubeJSPlugin {
    public void addBindings(@NotNull BindingsEvent event) {
        event.add("Lua", LuaJS.class);
    }

    @Override
    public void afterInit() {
        PeripheralRegisterEvent event = new PeripheralRegisterEvent(new ArrayList<>());

        super.afterInit();
    }

    @Override
    public void registerEvents() {
        ComputerCraftEvents.GROUP.register();
    }
}
