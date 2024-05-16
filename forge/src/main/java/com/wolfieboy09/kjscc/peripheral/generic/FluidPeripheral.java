package com.wolfieboy09.kjscc.peripheral.generic;

import com.wolfieboy09.kjscc.Utils;
import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.shared.peripheral.generic.methods.FluidMethods;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluidPeripheral extends PeripheralJS {

    FluidMethods fluidMethods;
    public FluidPeripheral() {
        super(null, "fluid_storage", new ArrayList<>());

        fluidMethods = new FluidMethods();
        mainThreadMethod("tanks", this::tanks);
        mainThreadMethod("pushFluid", this::pushFluid);
        mainThreadMethod("pullFluid", this::pullFluid);
    }

    public Object tanks (BlockContainerJS block, Direction side, List<?> arguments, IComputerAccess computer, ILuaContext context) {
        return fluidMethods.tanks(Utils.getFluidHandler(block));
    }

    public Object pushFluid (BlockContainerJS block, Direction side, List<?> arguments, IComputerAccess computer, ILuaContext context) throws LuaException {
        String argToName = Utils.castObjToString(arguments.get(0), "toName must be a string");
        Optional<Integer> argLimit = Optional.empty();
        Optional<String> argFluidName = Optional.ofNullable(arguments.size() > 2 ? Utils.castObjToString(arguments.get(2), "fluidName must be a string") : null);

        return fluidMethods.pushFluid(Utils.getFluidHandler(block), computer, argToName, argLimit, argFluidName);
    }

    public Object pullFluid (BlockContainerJS block, Direction side, List<?> arguments, IComputerAccess computer, ILuaContext context) throws LuaException {
        String argFromName = Utils.castObjToString(arguments.get(0), "fromName must be a string");
        Optional<Integer> argLimit = Optional.empty();
        Optional<String> argFluidName = Optional.ofNullable(arguments.size() > 2 ? Utils.castObjToString(arguments.get(2), "fluidName must be a string") : null);

        return fluidMethods.pullFluid(Utils.getFluidHandler(block), computer, argFromName, argLimit, argFluidName);
    }

    @Override
    public boolean test(@NotNull BlockContainerJS block) {
        BlockEntity ent = block.getEntity();

        if (ent != null) return ent.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent();
        return false;
    }
}