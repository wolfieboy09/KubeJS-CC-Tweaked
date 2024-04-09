package com.wolfieboy09.kjscc.result;

import dan200.computercraft.api.lua.MethodResult;
import dev.latvian.mods.rhino.NativeArray;
import dev.latvian.mods.rhino.ScriptableObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface IResultJS {
    static IResultJS getLuaType(Object o) {
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
