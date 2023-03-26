package de.hhn.stringcalculator.util;

public class FirstElementCheckResult {
    public final EquationElementType type;
    public final int length;
    public final boolean inverse;

    FirstElementCheckResult(EquationElementType type, int length, boolean inverse) {
        this.type = type;
        if (length < 0)
            length = 0;
        this.length = length;
        this.inverse = inverse;
    }

    FirstElementCheckResult(EquationElementType type, int length) {
        this.type = type;
        if (length < 0)
            length = 0;
        this.length = length;
        this.inverse = false;
    }

    FirstElementCheckResult(EquationElementType type, boolean inverse) {
        this.type = type;
        this.length = type == EquationElementType.UNKNOWN ? 0 : 1;
        this.inverse = inverse;
    }

    FirstElementCheckResult(EquationElementType type) {
        this.type = type;
        this.length = type == EquationElementType.UNKNOWN ? 0 : 1;
        this.inverse = false;
    }
}