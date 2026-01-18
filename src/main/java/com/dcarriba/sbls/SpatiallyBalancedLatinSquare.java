package com.dcarriba.sbls;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 * The {@link SpatiallyBalancedLatinSquare} class attempts to solve the problem of n*n Spatially Balanced Latin Square
 * for a given n.
 */
public class SpatiallyBalancedLatinSquare {
    /** Size of the Spatially Balanced Latin Square */
    private final int n;
    /** If the solution (if found) should be printed */
    private final boolean printSolution;
    /** {@link Model} of the problem */
    private Model model;
    /**
     * Variables of the problem <p>
     * Forall i and j in [0, n-1], variables[i][j] is the value of the cell on the i-th row and j-th column of the n*n
     * square <p>
     * The domain forall variables[i][j] is {0, ..., n-1}
     */
    private IntVar[][] variables;
    /** {@link Solver} of the {@link Model} of the problem */
    private Solver solver;

    /**
     * Constructor of the {@link SpatiallyBalancedLatinSquare} class
     *
     * @param n size of the Spatially Balanced Latin Square
     * @param printSolution if the solution (if found) should be printed
     */
    public SpatiallyBalancedLatinSquare(int n, boolean printSolution) {
        this.n = n;
        this.printSolution = printSolution;
        instantiateProblem();
    }

    /**
     * Instantiates the Spatially Balanced Latin Square Problem
     */
    private void instantiateProblem() {
        model = new Model(n + "*" + n + " Spatially Balanced Latin Square");
        variables = model.intVarMatrix(n, n, 0, n-1);

        postLineAllDiffConstraint();
        postColumnAllDiffConstraint();

        solver = model.getSolver();

        System.out.println(model.getName() + " problem has been instantiated.\n");
    }

    /**
     * Posts the following constraint to the model : <p>
     * for all lines, all values inside a line must be different from another
     */
    private void postLineAllDiffConstraint() {
        for (int i = 0; i < n; i++) {
            model.allDifferent(variables[i]).post();
        }
    }

    /**
     * Posts the following constraint to the model : <p>
     * for all columns, all values inside a column must be different from another
     */
    private void postColumnAllDiffConstraint() {
        for (int j = 0; j < n; j++) {
            IntVar[] column = new IntVar[n];

            for (int i = 0; i < n; i++) {
                column[i] = variables[i][j];
            }

            model.allDifferent(column).post();
        }
    }

    /**
     * Attempts to find a solution of the problem and prints the resolution statistics
     */
    public void solveProblem() {
        System.out.println("solving problem...\n");
        Solution solution = solver.findSolution();

        if (solution != null) {
            System.out.println("Solution found.\n");
            if (printSolution) {
                printSolution();
            }
        } else {
            System.out.println("No solution found\n");
        }

        System.out.println("Resolution Statistics:");
        solver.printStatistics();
    }

    /**
     * Prints the found solution
     */
    private void printSolution() {
        System.out.println("Solution:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(variables[i][j].getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
