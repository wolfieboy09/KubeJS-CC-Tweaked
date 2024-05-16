package com.wolfieboy09.kjscc.result;

import dan200.computercraft.api.lua.MethodResult;

import java.util.Arrays;

public class MultiResultJS implements IResultJS {
    private final IResultJS[] results;

    public MultiResultJS(IResultJS... results) {
        this.results = results;
    }

    @Override
    public Object getConvertedResult() {
        return Arrays.stream(results).map(IResultJS::getConvertedResult).toArray();
    }

    @Override
    public MethodResult getResult() {
        return MethodResult.of(Arrays.stream(results).map(IResultJS::getConvertedResult).toArray());
    }
}