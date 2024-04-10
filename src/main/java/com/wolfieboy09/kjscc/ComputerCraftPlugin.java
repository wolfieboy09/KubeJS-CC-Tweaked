package com.wolfieboy09.kjscc;

import com.wolfieboy09.kjscc.events.ComputerCraftEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.jetbrains.annotations.NotNull;

public class ComputerCraftPlugin extends KubeJSPlugin {
    public void addBindings(@NotNull BindingsEvent event) {
        event.add("Lua", LuaJS.class);
    }

    @Override
    public void registerEvents() {
        ComputerCraftEvents.GROUP.register();
    }
}
