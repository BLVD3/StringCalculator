package de.hhn.stringcalculator;

public class EquationPotentiation extends EquationOperation {
    public EquationPotentiation(EquationPart left, EquationPart right) {
        super(left, right);
    }

    @Override
    public double getValue(double... variables) {
        return Math.pow(left.getValue(variables), right.getValue(variables));
    }

    @Override
    public String toString() {
        return left.toString() + " ^ " + right.toString();
    }
}
