package de.hhn.stringcalculator;

public class EquationAddition extends EquationOperation {
    boolean inverted;

    public EquationAddition(EquationPart left, EquationPart right, boolean inverted) {
        super(left, right);
        this.inverted = inverted;
    }

    @Override
    public double getValue(double... variables) {
        return left.getValue(variables) + right.getValue(variables) * (inverted ? -1 : 1);
    }

    @Override
    public String toString() {
        return left.toString() + " + " + right.toString();
    }
}
