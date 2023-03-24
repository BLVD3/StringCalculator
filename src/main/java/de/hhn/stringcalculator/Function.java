package de.hhn.stringcalculator;

import de.hhn.stringcalculator.util.EquationValidator;
import de.hhn.stringcalculator.util.FirstElementCheckResult;
import de.hhn.stringcalculator.util.FunctionParseResult;

public class Function implements EquationPart {
    private final EquationPart function;

    public Function(EquationPart function) {
        this.function = function;
    }

    @Override
    public double getValue(double... variables) {
        return function.getValue(variables);
    }

    @Override
    public String toString() {
        return function.toString();
    }
}