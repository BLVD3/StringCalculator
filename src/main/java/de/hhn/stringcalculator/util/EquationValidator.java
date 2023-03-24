package de.hhn.stringcalculator.util;

import de.hhn.stringcalculator.EquationPart;
import de.hhn.stringcalculator.Function;

import java.lang.annotation.ElementType;

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
            FirstElementCheckResult currentElement = checkFirstElement(equation.substring(i), state == StateOfString.START);
            if (currentElement.type == EquationElementType.UNKNOWN)
                return false;
            //Switch returns with false if current Element does not make sense after
            switch (state) {
                case START:
                    switch (currentElement.type) {
                        case BRACKET_CLOSE, POWER_OPERATOR, MULTIPLICATION_OPERATOR, ADDITION_OPERATOR -> { return false; }
                    }
                    break;
                case NUMERIC:
                    if (currentElement.type == EquationElementType.NUMBER) {
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
            state = getIntendedState(currentElement);
            // Changes Bracket-count if needed
            if (currentElement.type == EquationElementType.BRACKET_OPEN)
                bracketCount++;
            if (currentElement.type == EquationElementType.BRACKET_CLOSE)
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

    private static StateOfString getIntendedState(FirstElementCheckResult currentElement){
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
    public static FirstElementCheckResult checkFirstElement(String equation, boolean startOfEquation) {
        boolean inverse = false;
        FirstElementCheckResult next;
        switch (equation.charAt(0)) {
            case '/':
                inverse = true;
            case '*':
                return new FirstElementCheckResult(EquationElementType.MULTIPLICATION_OPERATOR, inverse);
            case '^':
                return new FirstElementCheckResult(EquationElementType.POWER_OPERATOR);
            case '(':
                return new FirstElementCheckResult(EquationElementType.BRACKET_OPEN);
            case ')':
                return new FirstElementCheckResult(EquationElementType.BRACKET_CLOSE);
            case 'x', 'y',  'z':
                return new FirstElementCheckResult(EquationElementType.VARIABLE);
            case '-':
                if (!startOfEquation || equation.length() == 1)
                    return new FirstElementCheckResult(EquationElementType.ADDITION_OPERATOR, true);
                next = checkFirstElement(equation.substring(1), false);
                if (next.type == EquationElementType.VARIABLE)
                    return new FirstElementCheckResult(EquationElementType.VARIABLE, 2, true);
                if (next.type == EquationElementType.NUMBER)
                    return new FirstElementCheckResult(EquationElementType.NUMBER, next.length + 1, true);
                return new FirstElementCheckResult(EquationElementType.UNKNOWN);
            case '+':
                if (!startOfEquation || equation.length() == 1)
                    return new FirstElementCheckResult(EquationElementType.ADDITION_OPERATOR, false);
                next = checkFirstElement(equation.substring(1), false);
                if (next.type == EquationElementType.VARIABLE)
                    return new FirstElementCheckResult(EquationElementType.VARIABLE, 2, true);
                if (next.type == EquationElementType.NUMBER)
                    return new FirstElementCheckResult(EquationElementType.NUMBER, next.length + 1, true);
                return new FirstElementCheckResult(EquationElementType.UNKNOWN);
            case '0','1','2','3','4','5','6','7','8','9','.':
                return checkNumber(equation);
        }
        // TODO Functions
        return new FirstElementCheckResult(EquationElementType.UNKNOWN);
    }

    /**
     * Checks the length and viability of the Number.
     * @param equation
     * Equation in form of String
     * @return
     * Returns Information about the Number or Unknown
     */
    private static FirstElementCheckResult checkNumber(String equation) {
        if (!NUMERICS.contains(equation.substring(0,1)))
            return new FirstElementCheckResult(EquationElementType.UNKNOWN);
        boolean hasDecimal = equation.charAt(0) == '.';
        int count = 1;
        for (char c : equation.substring(1).toCharArray()) {
            if (c == '.') {
                if (hasDecimal)
                    return new FirstElementCheckResult(EquationElementType.UNKNOWN);
                hasDecimal = true;
            }
            if (!NUMERICS.contains(Character.toString(c)))
                return new FirstElementCheckResult(EquationElementType.NUMBER, count);
            count++;
        }
        return new FirstElementCheckResult(EquationElementType.NUMBER, count);
    }

    protected enum StateOfString {
        START,
        NUMERIC,
        VARIABLE,
        END_OF_BRACKET,
        OPERATOR,
        INVALID
    }

    public static void main(String[] args) {
        String equation = "(((4x)+3(3-67z)))";
        if (validateStringEquation(equation))
            System.out.println(removeBrackets(prepareStringEquation(equation)));
        else
            System.out.println(equation + " is not a valid Equation.");

    }

    public static FunctionParseResult parseEquation(String equation) {
        if (!validateStringEquation(equation))
            return new FunctionParseResult();
        equation = prepareStringEquation(equation);
        return new FunctionParseResult(new Function(makeEquationPart(equation)));
    }

    private static EquationPart makeEquationPart(String equation) {
        removeBrackets(equation);
        // Get last in order of operation
        // Make Toplayer of Binary Tree
        // If No Operation
        // Find Function
        // If no Function
        // Variable or Number
        return null;
    }

    private static String removeBrackets(String equation) {
        while(true)
        {
            if (equation.charAt(0) != '(')
                return equation;
            int bracketCount = 1;
            for (int i = 1; i < equation.length() - 1; i++) {
                if (equation.charAt(i) == '(')
                    bracketCount++;
                else if (equation.charAt(i) == ')') {
                    bracketCount--;
                    if (bracketCount == 0)
                        return equation;
                }
            }
            equation = equation.substring(1, equation.length() - 1);
        }
    }

    private static EquationElementType lastOperationInEquation(String equation) {
        int bracketCount = 0;
        int operationID = -1;
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '(')
                bracketCount++;
            else if (equation.charAt(i) == ')')
                bracketCount--;
            if (bracketCount == 0) {
                switch (equation.charAt(i)) {
                    case '+', '-' -> ;
                    case '*', '/' -> ;
                }
            }
        }
        return null;
    }

    private static String prepareStringEquation(String equation) {
        FirstElementCheckResult prev = checkFirstElement(equation, true);
        FirstElementCheckResult next;
        for (int i = prev.length; i < equation.length();) {
            next = checkFirstElement(equation.substring(i), getIntendedState(prev) == StateOfString.START);
            if (implicitMultiplication(prev.type, next.type)) {
                equation = equation.substring(0, i) + "*" + equation.substring(i);
                i++;
            }
            prev = next;
            i += next.length;
        }
        return equation;
    }

    private static boolean implicitMultiplication(EquationElementType first, EquationElementType second) {
        return switch (first) {
            case NUMBER, VARIABLE, BRACKET_CLOSE  -> switch (second) {
                case NUMBER, VARIABLE, BRACKET_OPEN -> true;
                default -> false;
            };
            default -> false;
        };
    }
}
