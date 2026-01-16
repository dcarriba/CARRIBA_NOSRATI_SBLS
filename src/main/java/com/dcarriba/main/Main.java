package com.dcarriba.main;

import com.dcarriba.sbls.SpatiallyBalancedLatinSquare;

/**
 * {@link Main} class of the program to solve the SBLS problem.
 *
 * <p>
 * Usage using the CLI:
 * <p>
 * <code>./gradlew run --args='-n N -printSolution BOOLEAN'</code>
 * <p>
 * with N the wanted value for n and BOOLEAN the boolean value whether the solution should be printed or not.
 * <p>
 * Example with n equals 6 and where the solution should be printed:
 * <p>
 * <code>./gradlew run --args='-n 6 -printSolution true'</code>
 */
public class Main {

    /**
     * Main function of the program
     *
     * @param args Arguments given to the program
     */
    public static void main(String[] args) {
        int n = getArgumentN(args);
        boolean printSolution = getArgumentPrintSolution(args);

        System.out.println("Spatially Balanced Latin Square");
        System.out.println("\nArguments:\nn = " + n + "\nprintSolution = " + printSolution + "\n");

        new SpatiallyBalancedLatinSquare(n, printSolution).solveProblem();
    }

    /**
     * Extracts the value for n from the given arguments
     *
     * @param args Arguments given to the program
     * @return value for n
     */
    private static int getArgumentN(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-n")) {
                if (i + 1 < args.length) {
                    try {
                        return Integer.parseInt(args[i + 1]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid value for n. Please provide an integer.");
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for -n.");
                }
            }
        }

        return 0;
    }

    /**
     * Extracts the value for printSolution from the given arguments
     *
     * @param args Arguments given to the program
     * @return value for printSolution
     */
    private static boolean getArgumentPrintSolution(String[] args) {
         for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-printSolution")) {
                if (i + 1 < args.length) {
                    return Boolean.parseBoolean(args[i + 1]);
                } else {
                    throw new IllegalArgumentException("Missing value for -printSolution.");
                }
            }
        }

        return false;
    }
}
