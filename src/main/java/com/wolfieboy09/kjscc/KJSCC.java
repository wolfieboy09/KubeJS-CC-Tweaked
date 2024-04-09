package com.wolfieboy09.kjscc;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("kjscc")
public class KJSCC {
    public static final String MOD_ID = "kjscc";
    public KJSCC() {
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
    }
}