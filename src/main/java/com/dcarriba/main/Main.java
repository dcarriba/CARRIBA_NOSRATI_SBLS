package com.dcarriba.main;

import com.dcarriba.sbls.AdvancedSpatiallyBalancedLatinSquare;
import com.dcarriba.sbls.SimpleSpatiallyBalancedLatinSquare;

/**
 * {@link Main} class of the program to solve the SBLS problem.
 *
 * <p>
 * Usage using the CLI:
 * <p>
 * <code>./gradlew run --args='-n N -method METHOD -printSolution BOOLEAN'</code>
 * <p>
 * with N the wanted value for n. <br>
 * with METHOD the resolution method that will be used. METHOD is either simple for the simple resolution method or
 * advanced for the advanced resolution method. <br>
 * with BOOLEAN the boolean value whether the solution should be printed or not.
 * <p>
 * Example with n equals 6, with the simple resolution method, and where the solution should be printed:
 * <p>
 * <code>./gradlew run --args='-n 6 -method simple -printSolution true'</code>
 * <p>
 * Example with n equals 6, with the advanced resolution method, and where the solution should be printed:
 * <p>
 * <code>./gradlew run --args='-n 6 -method advanced -printSolution true'</code>
 */
public class Main {

    /**
     * Main function of the program
     *
     * @param args Arguments given to the program
     */
    public static void main(String[] args) {
        int n = getArgumentN(args);
        String method = getArgumentMethod(args);
        boolean printSolution = getArgumentPrintSolution(args);

        System.out.println("Spatially Balanced Latin Square");
        System.out.println("\nArguments:\nn = " + n + "\nmethod = " + method + "\nprintSolution = " + printSolution + "\n");

        if (method.equals("simple")) {
            new SimpleSpatiallyBalancedLatinSquare(n, printSolution).solveProblem();
        } else if (method.equals("advanced")) {
            new AdvancedSpatiallyBalancedLatinSquare(n, printSolution).solveProblem();
        } else {
            throw new IllegalArgumentException("Unknown method: " + method);
        }
    }

    /**
     * Extracts the value for n from the given arguments
     *
     * @param args Arguments given to the program
     * @return     value for n
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
     * @return     value for printSolution
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

    /**
     * Extracts the value for method from the given arguments
     * 
     * @param args Arguments given to the program
     * @return     value for method
     */
    private static String getArgumentMethod(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-method")) {
                if (i + 1 < args.length) {
                    try {
                        String method = args[i + 1].toLowerCase();
                        if (method.equals("simple") || method.equals("advanced")) {
                            return method;
                        } else {
                            throw new IllegalArgumentException("Invalid method. Use simple or advanced.");
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid value for method. Use simple or advanced.");
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for -method.");
                }
            }
        }

        return "simple";
    }
}
