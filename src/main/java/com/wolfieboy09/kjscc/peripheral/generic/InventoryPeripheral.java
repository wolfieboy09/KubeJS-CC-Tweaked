package com.wolfieboy09.kjscc.peripheral.generic;

import com.wolfieboy09.kjscc.Utils;
import com.wolfieboy09.kjscc.peripheral.PeripheralJS;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.shared.peripheral.generic.methods.InventoryMethods;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryPeripheral extends PeripheralJS {
    InventoryMethods invMethods;

    public InventoryPeripheral() {
        super(null, "inventory", new ArrayList<>());

        invMethods = new InventoryMethods();
        mainThreadMethod("list", this::list);
        mainThreadMethod("size", this::size);
        mainThreadMethod("getItemDetail", this::getItemDetail);
        mainThreadMethod("getItemLimit", this::getItemLimit);
        mainThreadMethod("pushItems", this::pushItems);
        mainThreadMethod("pullItems", this::pullItems);
    }

    public Object list (BlockContainerJS block, Direction side, List<Object> arguments, IComputerAccess computer, ILuaContext context) {
        return InventoryMethods.list(Utils.getItemHandler(block));
    }

    public Object size (BlockContainerJS block, Direction side, List<Object> arguments, IComputerAccess computer, ILuaContext context) {
        return InventoryMethods.size(Utils.getItemHandler(block));
    }

    public Object getItemDetail (BlockContainerJS block, Direction side, List<Object> arguments, IComputerAccess computer, ILuaContext context) throws LuaException {
        int arg0 = Utils.castObjToInt(arguments.get(0), "Slot must be a valid integer");
        return InventoryMethods.getItemDetail(Utils.getItemHandler(block), arg0);
    }

    public Object getItemLimit (BlockContainerJS block, Direction side, List<Object> arguments, IComputerAccess computer, ILuaContext context) throws LuaException {
        int arg0 = Utils.castObjToInt(arguments.get(0), "Slot must be a valid integer");
        return InventoryMethods.getItemLimit(Utils.getItemHandler(block), arg0);
    }

    public Object pushItems (BlockContainerJS block, Direction side, List<Object> arguments, IComputerAccess computer, ILuaContext context) throws LuaException {
        String argToName = Utils.castObjToString(arguments.get(0), "toName must be a string");
        int argFromSlot = Utils.castObjToInt(arguments.get(1), "fromSlot must be a valid integer");
        Optional<Integer> argLimit = Optional.empty();
        Optional<Integer> argToSlot = Optional.empty();

        return InventoryMethods.pushItems(Utils.getItemHandler(block), computer, argToName, argFromSlot, argLimit, argToSlot);
    }

    public Object pullItems (BlockContainerJS block, Direction side, List<Object> arguments, IComputerAccess computer, ILuaContext context) throws LuaException {
        String argFromName = Utils.castObjToString(arguments.get(0), "fromName must be a string");
        int argFromSlot = Utils.castObjToInt(arguments.get(1), "fromSlot must be a valid integer");
        Optional<Integer> argLimit = Optional.empty();
        Optional<Integer> argToSlot = Optional.empty();

        return InventoryMethods.pullItems(Utils.getItemHandler(block), computer, argFromName, argFromSlot, argLimit, argToSlot);
    }

    @Override
    public boolean test(BlockContainerJS block) {
        BlockEntity ent = block.getEntity();

        if (ent != null) return ent.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent();
        return false;
    }

}
