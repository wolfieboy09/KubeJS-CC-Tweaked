package com.wolfieboy09.kjscc;

import com.wolfieboy09.kjscc.platform.Services;

public class CommonKJSCC {
    public static void init() {
        Constants.LOG.info("Loading {} | Environment: {}", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }
}