package com.wolfieboy09.kjscc.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class PeripheralProviderBase implements IPeripheralProvider {
    protected final List<PeripheralJS> peripherals;
    protected boolean invalidated = false;

    public PeripheralProviderBase(List<PeripheralJS> peripherals) { this.peripherals = peripherals; }

    protected List<PeripheralJS> getPeripheralJS(BlockContainerJS block) {
        return peripherals.stream().filter(p -> p.test(block)).toList();
    }

    public void invalidate() { this.invalidated = true; }

    @NotNull
    public LazyOptional<IPeripheral> getPeripheral(@NotNull Level world, @NotNull BlockPos pos, @NotNull Direction side) {
        if (invalidated) return LazyOptional.empty();

        List<PeripheralJS> match = getPeripheralJS(new BlockContainerJS(world, pos));
        if (!match.isEmpty()) {
            List<PeripheralMethod> allMethods = match.stream().flatMap(p -> p.getMethods().stream()).collect(Collectors.toList());
            return LazyOptional.of(() -> new DynamicPeripheralJS(match.size() > 1 ? "multiperipheral" : match.get(0).getType(), world, pos, side, allMethods));
        } else {
            return LazyOptional.empty();
        }
    }
}