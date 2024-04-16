package com.wolfieboy09.kjscc.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PeripheralProviderBase implements IPeripheralProvider {
    protected final List<PeripheralJS> peripherals;

    public PeripheralProviderBase(List<PeripheralJS> peripherals) {
        this.peripherals = peripherals;
    }

    protected Optional<PeripheralJS> getPeripheralJS(BlockState state) {
        return peripherals.stream().filter(p -> p.test(state)).findFirst();
    }

    @NotNull
    public LazyOptional<IPeripheral> getPeripheral(@NotNull Level world, @NotNull BlockPos pos, @NotNull Direction side) {
        PeripheralJS peripheral = getPeripheralJS(world.getBlockState(pos)).orElse(null);
        if (peripheral != null)
            return LazyOptional.of(() -> new DynamicPeripheralJS(peripheral.getType(), world, pos, side, peripheral.getMethods()));
        else
            return LazyOptional.empty();
    }
}