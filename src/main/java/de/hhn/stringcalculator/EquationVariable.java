package de.hhn.stringcalculator;

import java.util.HashMap;
import java.util.Map.Entry;

public class EquationVariable implements EquationPart {
    private final int i;

    private static final HashMap<Character, Integer> variableIds = new HashMap<>();
    private static boolean initDone = false;

    public EquationVariable(int id) {
        this.i = id;
    }

    @Override
    public double getValue(double... variables) {
        if (variables.length <= this.i)
            return 0;
        return variables[i];
    }

    /**
     * Method returns the intended id for the respective Variable name.
     * @param name
     * The Variable name
     * @return
     * -1 if Variable name is Unknown
     */
    public static int getId(char name) {
        if (!initDone)
            initStandardVariables();
        if (variableIds.containsKey(name))
            return variableIds.get(name);
        return -1;
    }

    public static void removeVariable(char name) {
        variableIds.remove(name);
    }

    private static void initStandardVariables() {
        variableIds.put('x', 0);
        variableIds.put('y', 1);
        variableIds.put('z', 2);
        initDone = true;
    }

    @Override
    public String toString() {
        for (Entry<Character, Integer> entry : variableIds.entrySet()) {
            if (entry.getValue() == this.i)
                return entry.getKey().toString();
        }
        return "?";
    }
}
