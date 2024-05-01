package com.wolfieboy09.kjscc.methods;

import com.wolfieboy09.kjscc.KJSCC;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

// Taken from
// https://github.com/cc-tweaked/CC-Tweaked/blob/mc-1.19.2/src/main/java/dan200/computercraft/shared/peripheral/generic/methods/EnergyMethods.java
public class EnergyMethods implements GenericPeripheral {
    @Nonnull
    @Override
    public PeripheralType getType()
    {
        return PeripheralType.ofAdditional( "energy_storage" );
    }

    @Nonnull
    @Override
    public ResourceLocation id()
    {
        return new ResourceLocation( KJSCC.MOD_ID, "energy" );
    }


    @LuaFunction( mainThread = true )
    public static int getEnergy(@NotNull IEnergyStorage energy )
    {
        return energy.getEnergyStored();
    }


    @LuaFunction( mainThread = true )
    public static int getEnergyCapacity(@NotNull IEnergyStorage energy )
    {
        return energy.getMaxEnergyStored();
    }
}
