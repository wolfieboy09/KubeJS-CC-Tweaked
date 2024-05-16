package com.wolfieboy09.kjscc;

import dan200.computercraft.api.lua.LuaException;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Utils {
    public static int castObjToInt (Object o, String error) throws LuaException {
        if (!(o instanceof Double d)) {
            throw new LuaException(error);
        }
        if (d.intValue() != d) {
            throw new LuaException(error);
        }
        return d.intValue();
    }

    @Contract(value = "null, _ -> fail", pure = true)
    public static @NotNull String castObjToString (Object o, String error) throws LuaException {
        if (!(o instanceof String)) {
            throw new LuaException(error);
        }
        return (String) o;
    }

    public static @NotNull IItemHandler getItemHandler(@NotNull BlockContainerJS block) {
        return Objects.requireNonNull(block.getEntity()).getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
    }

    public static @NotNull IFluidHandler getFluidHandler(@NotNull BlockContainerJS block) {
        return Objects.requireNonNull(block.getEntity()).getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
    }

    public static @NotNull IEnergyStorage getEnergyStorage(@NotNull BlockContainerJS block) {
        return Objects.requireNonNull(block.getEntity()).getCapability(ForgeCapabilities.ENERGY).resolve().get();
    }
}

