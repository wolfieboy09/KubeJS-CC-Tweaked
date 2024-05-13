package com.wolfieboy09.KJSCC;

import com.wolfieboy09.KJSCC.platform.Services;

public class CommonKJSCC {
    public static void init() {
        Constants.LOG.info("Loading {} | Environment: {}", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }
}