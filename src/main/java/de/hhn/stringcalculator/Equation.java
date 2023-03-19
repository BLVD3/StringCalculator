package de.hhn.stringcalculator;

/**
 * Calculator which is capable of transforming a String into a functioning Equation.
 * It has the following capability's:
 *  - Addition
 *  - Subtraction
 *  - Multiplication
 *  - 
 */
public class Equation {
    private static final String NUMERICS = "0123456789.";

    public static boolean validateString(String equation) {
        StateOfString state = StateOfString.START;
        int bracketCount = 0;
        for (char c : equation.toCharArray()) {
            switch (state) {
                case START:

                    break;
            }
        }
        return false;
    }

    public static EquationElement checkFirstElement(String equation, boolean startOfEquation) {
        boolean inverse = false;
        EquationElement next;
        switch (equation.charAt(0)) {
            case '/':
                inverse = true;
            case '*':
                return new EquationElement(ElementType.MULTIPLICATION_OPERATOR, inverse);
            case '^':
                return new EquationElement(ElementType.POWER_OPERATOR);
            case '(':
                return new EquationElement(ElementType.BRACKET_OPEN);
            case ')':
                return new EquationElement(ElementType.BRACKET_CLOSE);
            case 'x':
                return new EquationElement(ElementType.VARIABLE);
            case '-':
                if (!startOfEquation || equation.length() == 1)
                    return new EquationElement(ElementType.ADDITION_OPERATOR, true);
                next = checkFirstElement(equation.substring(1), false);
                if (next.type == ElementType.VARIABLE)
                    return new EquationElement(ElementType.VARIABLE, 2, true);
                if (next.type == ElementType.NUMBER)
                    return new EquationElement(ElementType.NUMBER, next.length + 1, true);
                return new EquationElement(ElementType.UNKNOWN);
            case '+':
                if (!startOfEquation || equation.length() == 1)
                    return new EquationElement(ElementType.ADDITION_OPERATOR, false);
                next = checkFirstElement(equation.substring(1), false);
                if (next.type == ElementType.VARIABLE)
                    return new EquationElement(ElementType.VARIABLE, 2, true);
                if (next.type == ElementType.NUMBER)
                    return new EquationElement(ElementType.NUMBER, next.length + 1, true);
                return new EquationElement(ElementType.UNKNOWN);
            case '0','1','2','3','4','5','6','7','8','9','.':
                return checkNumber(equation, false);
        }
        return new EquationElement(ElementType.UNKNOWN);
    }

    private static EquationElement checkNumber(String equation, boolean inverse) {
        boolean hasDecimal = equation.charAt(0) == '.';
        int count = 1;
        for (char c : equation.substring(1).toCharArray()) {
            if (c == '.') {
                if (hasDecimal)
                    return new EquationElement(ElementType.UNKNOWN);
                hasDecimal = true;
            }
            if (!NUMERICS.contains(Character.toString(c)))
                return new EquationElement(ElementType.NUMBER, count, inverse);
            count++;
        }
        return new EquationElement(ElementType.NUMBER, count, inverse);
    }

    protected enum ElementType {
        NUMBER,
        DECIMAL,
        VARIABLE,
        ADDITION_OPERATOR,
        MULTIPLICATION_OPERATOR,
        POWER_OPERATOR,
        BRACKET_OPEN,
        BRACKET_CLOSE,
        FUNCTION,
        UNKNOWN
    }

    protected static class EquationElement {
        public final ElementType type;
        public final int length;
        public boolean inverse;

        EquationElement(ElementType type, int length, boolean inverse) {
            this.type = type;
            if (length < 0)
                length = 0;
            this.length = length;
            this.inverse = inverse;
        }

        EquationElement(ElementType type, int length) {
            this.type = type;
            if (length < 0)
                length = 0;
            this.length = length;
            this.inverse = false;
        }

        EquationElement(ElementType type, boolean inverse) {
            this.type = type;
            this.length = type == ElementType.UNKNOWN ? 0 : 1;
            this.inverse = inverse;
        }

        EquationElement(ElementType type) {
            this.type = type;
            this.length = type == ElementType.UNKNOWN ? 0 : 1;
            this.inverse = false;
        }
    }

    protected enum StateOfString {
        START,
        NUMERIC,
        DECIMAL,
        OPERATOR,
    }

    public static void main(String[] args) {
    }
}