package com.wolfieboy09.kjscc.events;


import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class ComputerCraftEvents {
    public static final EventGroup GROUP = EventGroup.of("ComputerCraftEvents");

    EventHandler PERIPHERAL = ComputerCraftEvents.GROUP.startup("peripheral", () ->  PeripheralJS.class);
}
