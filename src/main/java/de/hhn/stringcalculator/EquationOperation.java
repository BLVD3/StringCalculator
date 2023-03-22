package de.hhn.stringcalculator;

public abstract class EquationOperation implements EquationPart {
    protected final EquationPart left;
    protected final EquationPart right;

    public EquationOperation(EquationPart left, EquationPart right) {
        this.left = left;
        this.right = right;
    }
}
