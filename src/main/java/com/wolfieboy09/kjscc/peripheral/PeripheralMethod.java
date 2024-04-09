package com.wolfieboy09.kjscc.peripheral;

public record PeripheralMethod(String type, DynamicPeripheralJS.PeripheralCallback callback, boolean mainThread) {
    public PeripheralMethod(String type, DynamicPeripheralJS.PeripheralCallback callback) {
        this(type, callback, false);
    }
}