package com.wolfieboy09.kjscc;

import com.wolfieboy09.kjscc.result.IResultJS;
import com.wolfieboy09.kjscc.result.MarkerUnpackedResult;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LuaJS {
    public static @NotNull MarkerUnpackedResult pack(@NotNull Object... objects) {
        return new MarkerUnpackedResult(objects);
    }

    //Wraps js method into lua method because we need explicit annotation here.
    //Also, post-processing of result is important.
    public static @NotNull ILuaFunction method(Function<IArguments, Object> method) {
        return arguments -> IResultJS.getLuaType(method.apply(arguments)).getResult();
    }
}