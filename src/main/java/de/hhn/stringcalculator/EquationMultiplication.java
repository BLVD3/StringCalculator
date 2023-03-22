package de.hhn.stringcalculator;

public class EquationMultiplication extends EquationOperation {
    boolean inverted;
    public EquationMultiplication(EquationPart left, EquationPart right, boolean inverted) {
        super(left, right);
        this.inverted = inverted;
    }

    @Override
    public double getValue(double... variables) {
        return left.getValue(variables) / (inverted ? left.getValue(variables) : 1 / left.getValue(variables));
    }

    @Override
    public String toString() {
        return left.toString() + " " + (inverted ? '/' : '*') + " " + right.toString();
    }
}
