package com.wolfieboy09.kjscc;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.jetbrains.annotations.NotNull;

public class ComputerCraftPlugin extends KubeJSPlugin {
    public void addBindings(@NotNull BindingsEvent event) {
        event.add("Lua", LuaJS.class);
    }
}
