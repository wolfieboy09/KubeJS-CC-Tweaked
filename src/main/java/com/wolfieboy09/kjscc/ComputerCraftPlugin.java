package com.wolfieboy09.kjscc;

import java.util.ArrayList;
import java.util.List;

import com.wolfieboy09.kjscc.peripheral.PeripheralProviderBase;
import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import com.wolfieboy09.kjscc.events.ComputerCraftEvents;
import com.wolfieboy09.kjscc.peripheral.generic.FluidPeripheral;
import com.wolfieboy09.kjscc.peripheral.generic.InventoryPeripheral;
import com.wolfieboy09.kjscc.peripheral.generic.EnergyPeripheral;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.jetbrains.annotations.NotNull;

public class ComputerCraftPlugin extends KubeJSPlugin {
    private PeripheralProviderBase provider;
    private boolean firstReload = true;

    public void addBindings(@NotNull BindingsEvent event) {
        event.add("Lua", LuaJS.class);
    }

    @Override
    public void afterInit() {
        super.afterInit();

        List<PeripheralJS> peripheralsList = new ArrayList<>();
        peripheralsList.add(new InventoryPeripheral());
        peripheralsList.add(new FluidPeripheral());
        peripheralsList.add(new EnergyPeripheral());

        PeripheralRegisterEvent event = new PeripheralRegisterEvent(peripheralsList);
        ComputerCraftEvents.PERIPHERAL.post(ScriptType.STARTUP, event);
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
