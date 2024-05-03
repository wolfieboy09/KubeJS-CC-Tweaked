package com.wolfieboy09.kjscc.peripheral.generic;

import com.wolfieboy09.kjscc.Utils;
import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.shared.peripheral.generic.methods.EnergyMethods;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnergyPeripheral extends PeripheralJS {

    EnergyMethods energyMethods;
    public EnergyPeripheral() {
        super(null, "energy_storage", new ArrayList<>());

        energyMethods = new EnergyMethods();
        mainThreadMethod("getEnergy", this::getEnergy);
        mainThreadMethod("getEnergyCapacity", this::getEnergyCapacity);
    }

    public Object getEnergy (BlockContainerJS block, Direction side, List arguments, IComputerAccess computer, ILuaContext context) {
        return energyMethods.getEnergy(Utils.getEnergyStorage(block));
    }

    public Object getEnergyCapacity (BlockContainerJS block, Direction side, List arguments, IComputerAccess computer, ILuaContext context) {
        return energyMethods.getEnergyCapacity(Utils.getEnergyStorage(block));
    }

    @Override
    public boolean test(@NotNull BlockContainerJS block) {
        BlockEntity ent = block.getEntity();

        if (ent != null) return ent.getCapability(ForgeCapabilities.ENERGY).isPresent();
        return false;
    }
}
