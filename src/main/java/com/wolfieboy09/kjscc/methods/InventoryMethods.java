package com.wolfieboy09.kjscc.methods;

import com.wolfieboy09.kjscc.KJSCC;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.wolfieboy09.kjscc.methods.ArgumentHelpers.assertBetween;


public class InventoryMethods implements GenericPeripheral {
    @Nonnull
    @Override
    public PeripheralType getType()
    {
        return PeripheralType.ofAdditional( "inventory" );
    }

    @Nonnull
    @Override
    public ResourceLocation id()
    {
        return new ResourceLocation( KJSCC.MOD_ID, "inventory" );
    }

    /**
     * Get the size of this inventory.
     *
     * @param inventory The current inventory.
     * @return The number of slots in this inventory.
     */
    @LuaFunction( mainThread = true )
    public static int size(@NotNull IItemHandler inventory )
    {
        return inventory.getSlots();
    }


    @LuaFunction( mainThread = true )
    public static @NotNull Map<Integer, Map<String, ?>> list(@NotNull IItemHandler inventory )
    {
        Map<Integer, Map<String, ?>> result = new HashMap<>();
        int size = inventory.getSlots();
        for( int i = 0; i < size; i++ )
        {
            ItemStack stack = inventory.getStackInSlot( i );
            if( !stack.isEmpty() ) result.put( i + 1, VanillaDetailRegistries.ITEM_STACK.getBasicDetails( stack ) );
        }

        return result;
    }


    @Nullable
    @LuaFunction( mainThread = true )
    public static Map<String, ?> getItemDetail(@NotNull IItemHandler inventory, int slot ) throws LuaException
    {
        assertBetween( slot, 1, inventory.getSlots(), "Slot out of range (%s)" );

        ItemStack stack = inventory.getStackInSlot( slot - 1 );
        return stack.isEmpty() ? null : VanillaDetailRegistries.ITEM_STACK.getDetails( stack );
    }


    @LuaFunction( mainThread = true )
    public static int getItemLimit(@NotNull IItemHandler inventory, int slot ) throws LuaException
    {
        assertBetween( slot, 1, inventory.getSlots(), "Slot out of range (%s)" );
        return inventory.getSlotLimit( slot - 1 );
    }


    @LuaFunction( mainThread = true )
    public static int pushItems(
            IItemHandler from, @NotNull IComputerAccess computer,
            String toName, int fromSlot, Optional<Integer> limit, Optional<Integer> toSlot
    ) throws LuaException
    {
        // Find location to transfer to
        IPeripheral location = computer.getAvailablePeripheral( toName );
        if( location == null ) throw new LuaException( "Target '" + toName + "' does not exist" );

        IItemHandler to = extractHandler( location.getTarget() );
        if( to == null ) throw new LuaException( "Target '" + toName + "' is not an inventory" );

        // Validate slots
        int actualLimit = limit.orElse( Integer.MAX_VALUE );
        assertBetween( fromSlot, 1, from.getSlots(), "From slot out of range (%s)" );
        if( toSlot.isPresent() ) assertBetween( toSlot.get(), 1, to.getSlots(), "To slot out of range (%s)" );

        if( actualLimit <= 0 ) return 0;
        return moveItem( from, fromSlot - 1, to, toSlot.orElse( 0 ) - 1, actualLimit );
    }


    @LuaFunction( mainThread = true )
    public static int pullItems(
            IItemHandler to, @NotNull IComputerAccess computer,
            String fromName, int fromSlot, Optional<Integer> limit, Optional<Integer> toSlot
    ) throws LuaException
    {
        // Find location to transfer to
        IPeripheral location = computer.getAvailablePeripheral( fromName );
        if( location == null ) throw new LuaException( "Source '" + fromName + "' does not exist" );

        IItemHandler from = extractHandler( location.getTarget() );
        if( from == null ) throw new LuaException( "Source '" + fromName + "' is not an inventory" );

        // Validate slots
        int actualLimit = limit.orElse( Integer.MAX_VALUE );
        assertBetween( fromSlot, 1, from.getSlots(), "From slot out of range (%s)" );
        if( toSlot.isPresent() ) assertBetween( toSlot.get(), 1, to.getSlots(), "To slot out of range (%s)" );

        if( actualLimit <= 0 ) return 0;
        return moveItem( from, fromSlot - 1, to, toSlot.orElse( 0 ) - 1, actualLimit );
    }

    @Nullable
    private static IItemHandler extractHandler( @Nullable Object object )
    {
        if( object instanceof BlockEntity blockEntity && blockEntity.isRemoved() ) return null;

        if( object instanceof ICapabilityProvider provider )
        {
            LazyOptional<IItemHandler> cap = provider.getCapability( ForgeCapabilities.ITEM_HANDLER );
            if( cap.isPresent() ) return cap.orElseThrow( NullPointerException::new );
        }

        if( object instanceof IItemHandler handler ) return handler;
        if( object instanceof Container container ) return new InvWrapper( container );
        return null;
    }

    private static int moveItem(@NotNull IItemHandler from, int fromSlot, IItemHandler to, int toSlot, final int limit )
    {
        // See how much we can get out of this slot
        ItemStack extracted = from.extractItem( fromSlot, limit, true );
        if( extracted.isEmpty() ) return 0;

        // Limit the amount to extract
        int extractCount = Math.min( extracted.getCount(), limit );
        extracted.setCount( extractCount );

        ItemStack remainder = toSlot < 0 ? ItemHandlerHelper.insertItem( to, extracted, false ) : to.insertItem( toSlot, extracted, false );
        int inserted = remainder.isEmpty() ? extractCount : extractCount - remainder.getCount();
        if( inserted <= 0 ) return 0;

        // Remove the item from the original inventory. Technically this could fail, but there's little we can do
        // about that.
        from.extractItem( fromSlot, inserted, false );
        return inserted;
    }
}