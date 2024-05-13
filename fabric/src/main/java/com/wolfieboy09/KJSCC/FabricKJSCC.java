package com.wolfieboy09.KJSCC;

import net.fabricmc.api.ModInitializer;

public class FabricKJSCC implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("KubeJS + CC: Tweaked for Fabric has been called.");
        CommonKJSCC.init();
    }
}