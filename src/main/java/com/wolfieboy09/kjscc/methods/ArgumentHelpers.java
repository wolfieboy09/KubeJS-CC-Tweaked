package com.wolfieboy09.kjscc.methods;

import dan200.computercraft.api.lua.LuaException;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;


final class ArgumentHelpers
{
    private ArgumentHelpers()
    {
    }

    public static void assertBetween( double value, double min, double max, String message ) throws LuaException
    {
        if( value < min || value > max || Double.isNaN( value ) )
        {
            throw new LuaException( String.format( message, "between " + min + " and " + max ) );
        }
    }

    public static void assertBetween( int value, int min, int max, String message ) throws LuaException
    {
        if( value < min || value > max )
        {
            throw new LuaException( String.format( message, "between " + min + " and " + max ) );
        }
    }

    @Nonnull
    public static <T> T getRegistryEntry( String name, String typeName, IForgeRegistry<T> registry ) throws LuaException
    {
        ResourceLocation id;
        try
        {
            id = new ResourceLocation( name );
        }
        catch( ResourceLocationException e )
        {
            id = null;
        }

        T value;
        if( id == null || !registry.containsKey( id ) || (value = registry.getValue( id )) == null )
        {
            throw new LuaException( String.format( "Unknown %s '%s'", typeName, name ) );
        }

        return value;
    }
}