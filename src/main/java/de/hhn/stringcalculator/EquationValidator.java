package de.hhn.stringcalculator;

public class EquationValidator {
    /**
     * String of characters that represent parts of numbers
     */
    private static final String NUMERICS = "0123456789.";

    private EquationValidator() {  }

    /**
     * Checks if the provided String is parseable into an Equation
     * @param equation
     * String to be checked
     */
    public static boolean validateStringEquation(String equation) {
        StateOfString state = StateOfString.START;
        int bracketCount = 0;
        for (int i = 0; i < equation.length();) {
            EquationElement currentElement = checkFirstElement(equation.substring(i), state == StateOfString.START);
            if (currentElement.type == ElementType.UNKNOWN)
                return false;
            //Switch returns with false if current Element does not make sense after
            switch (state) {
                case START:
                    switch (currentElement.type) {
                        case BRACKET_CLOSE, POWER_OPERATOR, MULTIPLICATION_OPERATOR, ADDITION_OPERATOR -> { return false; }
                    }
                    break;
                case NUMERIC:
                    if (currentElement.type == ElementType.NUMBER) {
                        return false;
                    }
                    break;
                case OPERATOR:
                    switch (currentElement.type) {
                        case BRACKET_CLOSE, POWER_OPERATOR, ADDITION_OPERATOR, MULTIPLICATION_OPERATOR -> { return false; }
                    }
                    break;
                case END_OF_BRACKET, VARIABLE:
                    break;
                default:
                    return false;
            }
            // Sets the State for next check
            state = setState(currentElement);
            // Changes Bracket-count if needed
            if (currentElement.type == ElementType.BRACKET_OPEN)
                bracketCount++;
            if (currentElement.type == ElementType.BRACKET_CLOSE)
                bracketCount--;
            //returns false if Brackets are placed incorrectly
            if (bracketCount < 0)
                return false;
            i += currentElement.length;
        }
        if (bracketCount != 0)
            return false;
        //Checks if Last state is valid and returns
        return state != StateOfString.START && state != StateOfString.OPERATOR;
    }

    private static StateOfString setState(EquationElement currentElement){
        return switch (currentElement.type) {
            case VARIABLE -> StateOfString.VARIABLE;
            case NUMBER -> StateOfString.NUMERIC;
            case BRACKET_OPEN -> StateOfString.START;
            case BRACKET_CLOSE -> StateOfString.END_OF_BRACKET;
            case POWER_OPERATOR, ADDITION_OPERATOR, MULTIPLICATION_OPERATOR -> StateOfString.OPERATOR;
            default -> StateOfString.INVALID;
        };
    }


    /**
     * Attempts to find out, what the first Element of the given Equation is.
     * @param equation
     * Equation in form of String.
     * @param startOfEquation
     * Set true if the given Equation is preceded by nothing or '('.
     * @return
     * Object containing information about the type, length and inversion of the Element.
     */
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
            case 'x', 'y':
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
                return checkNumber(equation);
        }
        // TODO Functions
        return new EquationElement(ElementType.UNKNOWN);
    }

    /**
     * Checks the length and viability of the Number.
     * @param equation
     * Equation in form of String
     * @return
     * Returns Information about the Number or Unknown
     */
    private static EquationElement checkNumber(String equation) {
        if (!NUMERICS.contains(equation.substring(0,1)))
            return new EquationElement(ElementType.UNKNOWN);
        boolean hasDecimal = equation.charAt(0) == '.';
        int count = 1;
        for (char c : equation.substring(1).toCharArray()) {
            if (c == '.') {
                if (hasDecimal)
                    return new EquationElement(ElementType.UNKNOWN);
                hasDecimal = true;
            }
            if (!NUMERICS.contains(Character.toString(c)))
                return new EquationElement(ElementType.NUMBER, count);
            count++;
        }
        return new EquationElement(ElementType.NUMBER, count);
    }

    protected enum ElementType {
        NUMBER,
        VARIABLE,
        ADDITION_OPERATOR,
        MULTIPLICATION_OPERATOR,
        POWER_OPERATOR,
        BRACKET_OPEN,
        BRACKET_CLOSE,
        //FUNCTION,
        UNKNOWN
    }

    protected static class EquationElement {
        public final ElementType type;
        public final int length;
        public final boolean inverse;

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
        VARIABLE,
        END_OF_BRACKET,
        OPERATOR,
        INVALID
    }
}
