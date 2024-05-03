package com.wolfieboy09.kjscc.events;

import com.wolfieboy09.kjscc.PeripheralRegisterEvent;
import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import dev.latvian.mods.kubejs.event.EventExit;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

import java.util.List;

public interface ComputerCraftEvents {
    EventGroup GROUP = EventGroup.of("ComputerCraftEvents");

    EventHandler PERIPHERAL = ComputerCraftEvents.GROUP.startup("peripheral", () ->  PeripheralJS.class).hasResult();

    static void PERIPHERAL(PeripheralRegisterEvent event) throws EventExit {
    }
}
