package de.hhn.stringcalculator.util;

import de.hhn.stringcalculator.Function;

public class FunctionParseResult {
    private Function result;
    public final boolean success;

    public FunctionParseResult(Function result) {
        this.success = true;
        this.result = result;
    }

    public FunctionParseResult() {
        this.success = false;
    }

    public Function getResult() {
        return result;
    }
}
