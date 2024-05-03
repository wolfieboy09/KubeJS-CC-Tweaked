package com.wolfieboy09.kjscc.result;

import dan200.computercraft.api.lua.MethodResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public interface IResultJS {
    @Contract("null -> new")
    static @NotNull IResultJS getLuaType(Object o) {
        if (o instanceof MarkerUnpackedResult results) {
            return new MultiResultJS(Arrays.stream(results.getResults()).map(IResultJS::getLuaType).toArray(IResultJS[]::new));
        }

        return new ResultJS(o);
    }

    Object getConvertedResult();

    default MethodResult getResult() {
        return MethodResult.of(getConvertedResult());
    }
}
