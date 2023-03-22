package de.hhn.stringcalculator;

public class EquationNumber implements EquationPart {
    private final double number;

    public EquationNumber(double value) {
        this.number = value;
    }

    @Override
    public double getValue(double... variables) {
        return number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
