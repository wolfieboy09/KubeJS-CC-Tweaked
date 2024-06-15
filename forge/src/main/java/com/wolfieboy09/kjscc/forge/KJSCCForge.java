package com.wolfieboy09.kjscc.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.wolfieboy09.kjscc.KJSCC;

@Mod(KJSCC.MOD_ID)
public final class KJSCCForge {
    public KJSCCForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(KJSCC.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        KJSCC.init();
    }
}
