package com.wolfieboy09.kjscc;

import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.ArrayList;
import java.util.List;

public class PeripheralRegisterEvent extends EventJS {

    public PeripheralRegisterEvent(List<PeripheralJS> peripherals) {
        this.peripherals = peripherals;
    }

    private final List<PeripheralJS> peripherals;

    public List<PeripheralJS> getPeripherals() {
        return peripherals;
    }

    public PeripheralJS registerPeripheral(String type, BlockStatePredicate block) {
        PeripheralJS peripheral = new PeripheralJS(block, type, new ArrayList<>());
        peripherals.add(peripheral);
        return peripheral;
    }
}
