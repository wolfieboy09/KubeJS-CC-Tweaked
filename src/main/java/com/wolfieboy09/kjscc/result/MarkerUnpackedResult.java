package com.wolfieboy09.kjscc.result;

// IntelliJ says it can be a record class to the following
// public record MarkerUnpackedResult(Object... results) {}
// idk, should I tho??
public class MarkerUnpackedResult {
    private final Object[] results;

    public MarkerUnpackedResult(Object... results) {
        this.results = results;
    }

    public Object[] getResults() {
        return results;
    }
}