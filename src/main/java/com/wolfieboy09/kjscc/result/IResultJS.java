package com.wolfieboy09.kjscc.result;

import dan200.computercraft.api.lua.MethodResult;

import java.util.Arrays;

public interface IResultJS {
    static IResultJS getLuaType(Object o) {
        if (o instanceof MarkerUnpackedResult results) {
            return new MultiResultJS(Arrays.stream(results.results()).map(IResultJS::getLuaType).toArray(IResultJS[]::new));
        }

        return new ResultJS(o);
    }

    Object getConvertedResult();

    default MethodResult getResult() {
        return MethodResult.of(getConvertedResult());
    }
}
