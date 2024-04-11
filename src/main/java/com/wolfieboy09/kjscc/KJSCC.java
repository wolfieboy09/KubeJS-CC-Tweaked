package com.wolfieboy09.kjscc;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("kjscc")
public class KJSCC {
    public static final String MOD_ID = "kjscc";
    public static final Logger LOGGER = LogUtils.getLogger();
    public KJSCC() {
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
    }
}