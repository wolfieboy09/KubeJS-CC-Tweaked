package com.wolfieboy09.kjscc.result;

public class ResultJS implements IResultJS {

    private final Object value;

    public ResultJS(Object value) {
        this.value = value;
    }

    @Override
    public Object getConvertedResult() {
        return this.value;
    }

}