package com.wolfieboy09.kjscc;

import dan200.computercraft.api.lua.LuaException;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class Utils {
    public static int castObjToInt (Object o, String error) throws LuaException {
        if (!(o instanceof Double)) {
            throw new LuaException(error);
        }
        Double d = (Double) o;
        if (d.intValue() != d) {
            throw new LuaException(error);
        }
        return d.intValue();
    }

    public static String castObjToString (Object o, String error) throws LuaException {
        if (!(o instanceof String)) {
            throw new LuaException(error);
        }
        return (String) o;
    }

    public static IItemHandler getItemHandler(BlockContainerJS block) {
        return block.getEntity().getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
    }

    public static IFluidHandler getFluidHandler(BlockContainerJS block) {
        return block.getEntity().getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
    }

    public static IEnergyStorage getEnergyStorage(BlockContainerJS block) {
        return block.getEntity().getCapability(ForgeCapabilities.ENERGY).resolve().get();
    }

}
