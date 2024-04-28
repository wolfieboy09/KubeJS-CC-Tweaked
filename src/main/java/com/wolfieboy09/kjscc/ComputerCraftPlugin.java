package com.wolfieboy09.kjscc;

import java.util.ArrayList;
import com.wolfieboy09.kjscc.peripheral.PeripheralProviderBase;
import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import com.wolfieboy09.kjscc.events.ComputerCraftEvents;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.ScriptTypeHolder;
import org.jetbrains.annotations.NotNull;
import dev.latvian.mods.kubejs.event.EventJS;

public class ComputerCraftPlugin extends KubeJSPlugin {
    private PeripheralProviderBase provider;
    private boolean firstReload = true;

    public void addBindings(@NotNull BindingsEvent event) {
        event.add("Lua", LuaJS.class);
    }

    @Override
    public void afterInit() {
        super.afterInit();
        PeripheralRegisterEvent event = new PeripheralRegisterEvent(new ArrayList<PeripheralJS>());
        ComputerCraftEvents.PERIPHERAL.post((ScriptTypeHolder)ScriptType.STARTUP, (EventJS)event);
        provider = new PeripheralProviderBase(event.getPeripherals());
        ForgeComputerCraftAPI.registerPeripheralProvider(provider);
    }

    @Override
    public void onServerReload() {
        if (firstReload) {
            firstReload = false;
            return;
        }
        KJSCC.LOGGER.info("Server Reload Called");
        provider.invalidate();
        afterInit();
    }

    @Override
    public void registerEvents() {
        ComputerCraftEvents.GROUP.register();
    }
}
