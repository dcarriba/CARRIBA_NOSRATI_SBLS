package com.dcarriba.sbls;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link SpatiallyBalancedLatinSquare} class attempts to solve the problem of n*n Spatially Balanced Latin Square
 * for a given n.
 */
public abstract class SpatiallyBalancedLatinSquare {
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
     * @param n             size of the Spatially Balanced Latin Square
     * @param printSolution if the solution (if found) should be printed
     */
    public SpatiallyBalancedLatinSquare(int n, boolean printSolution) {
        this.n = n;
        this.printSolution = printSolution;
        instantiateProblem();
    }

    /**
     * @return Size of the Spatially Balanced Latin Square
     */
    public int getN() {
        return n;
    }

    /**
     * @return {@link Model} of the problem
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model {@link Model} of the problem
     */
    protected void setModel(Model model) {
        this.model = model;
    }

    /**
     * @return Variables of the problem
     */
    public IntVar[][] getVariables() {
        return variables;
    }

    /**
     * @param variables Variables of the problem
     */
    protected void setVariables(IntVar[][] variables) {
        this.variables = variables;
    }

    /**
     * @return {@link Solver} of the {@link Model} of the problem
     */
    public Solver getSolver() {
        return solver;
    }

    /**
     * @param solver {@link Solver} of the {@link Model} of the problem
     */
    protected void setSolver(Solver solver) {
        this.solver = solver;
    }

    /**
     * Instantiates the Spatially Balanced Latin Square Problem
     */
    protected abstract void instantiateProblem();

    /**
     * Posts the following constraint to the model: <p>
     * for all lines, all values inside a line must be different from another
     *
     * @param Consistency consistency level, among {"BC", "AC_REGIN", "AC", "AC_ZHANG", "DEFAULT"}
     */
    protected void postLineAllDiffConstraint(String Consistency) {
        for (int i = 0; i < n; i++) {
            model.allDifferent(variables[i], Consistency).post();
        }
    }

    /**
     * Posts the following constraint to the model: <p>
     * for all columns, all values inside a column must be different from another
     *
     * @param Consistency consistency level, among {"BC", "AC_REGIN", "AC", "AC_ZHANG", "DEFAULT"}
     */
    protected void postColumnAllDiffConstraint(String Consistency) {
        for (int j = 0; j < n; j++) {
            IntVar[] column = new IntVar[n];

            for (int i = 0; i < n; i++) {
                column[i] = variables[i][j];
            }

            model.allDifferent(column, Consistency).post();
        }
    }

    /**
     * Posts the spatially balanced constraint to the model: <p>
     * For all value pairs (elt1, elt2) inside the n*n square: the sum of their distances inside all lines is equal.
     * Same for the sum of the distances inside all columns.
     */
    protected void postSpatiallyBalancedConstraint() {
        // All sum of distances of all pairs (elt1, elt2)
        List<IntVar> allDistanceSums = new ArrayList<>();

        for (int elt1 = 0; elt1 < n; elt1++) {
            for (int elt2 = 0; elt2 < n; elt2++) {
                if (elt1 == elt2) continue;

                // Maximum possible distance sum for a pair
                int maxDistanceSum = n * n * (n - 1);

                IntVar distanceSumElt1Elt2InAllLines = model.intVar("lineSum(" + elt1 + "," + elt2 + ")", 0,
                        maxDistanceSum);

                List<IntVar> lineContributionsList = new ArrayList<>();
                int lineContributionIndex = 0;

                for (int i = 0; i < n; i++) {
                    for (int j1 = 0; j1 < n; j1++) {
                        for (int j2 = 0; j2 < n; j2++) {
                            int distance = Math.abs(j1 - j2);

                            BoolVar isElt1AtJ1 = model.boolVar("line_elt1_" + elt1 + "_at_" + i + "_" + j1 + "_"
                                    + lineContributionIndex);
                            BoolVar isElt2AtJ2 = model.boolVar("line_elt2_" + elt2 + "_at_" + i + "_" + j2 + "_"
                                    + lineContributionIndex);

                            model.reifyXeqC(variables[i][j1], elt1, isElt1AtJ1);
                            model.reifyXeqC(variables[i][j2], elt2, isElt2AtJ2);
                            
                            BoolVar bothActiveInRow = model.boolVar(elt1 + "_" + elt2 + "_line_active_"
                                    + lineContributionIndex);
                            model.times(isElt1AtJ1, isElt2AtJ2, bothActiveInRow).post();
                            
                            IntVar lineContribution = model.intVar( elt1 + "_" + elt2 + "_line_contribution_"
                                    + lineContributionIndex, 0, distance);
                            model.times(bothActiveInRow, distance, lineContribution).post();
                            
                            lineContributionsList.add(lineContribution);
                            lineContributionIndex++;
                        }
                    }
                }
                
                IntVar[] lineContributions = lineContributionsList.toArray(new IntVar[0]);
                model.sum(lineContributions, "=", distanceSumElt1Elt2InAllLines).post();

                IntVar sumOfDistancesOfElt1Elt2InAllColumns = model.intVar("colSum(" + elt1 + "," + elt2 + ")",
                        0, maxDistanceSum);

                List<IntVar> colContributionsList = new ArrayList<>();
                int colContribIndex = 0;
                
                for (int j = 0; j < n; j++) {
                    for (int i1 = 0; i1 < n; i1++) {
                        for (int i2 = 0; i2 < n; i2++) {
                            int distance = Math.abs(i1 - i2);
                            
                            BoolVar isElt1AtI1 = model.boolVar("col_elt1_" + elt1 + "_at_" + i1 + "_" + j + "_"
                                    + colContribIndex);
                            BoolVar isElt2AtI2 = model.boolVar("col_elt2_" + elt2 + "_at_" + i2 + "_" + j + "_"
                                    + colContribIndex);
                            
                            model.reifyXeqC(variables[i1][j], elt1, isElt1AtI1);
                            model.reifyXeqC(variables[i2][j], elt2, isElt2AtI2);
                            
                            BoolVar bothActiveInCol = model.boolVar(elt1 + "_" + elt2 + "_col_active_"
                                    + colContribIndex);
                            model.times(isElt1AtI1, isElt2AtI2, bothActiveInCol).post();
                            
                            IntVar colContribution = model.intVar(elt1 + "_" + elt2 + "_col_contribution_"
                                            + colContribIndex, 0, distance);
                            model.times(bothActiveInCol, distance, colContribution).post();
                            
                            colContributionsList.add(colContribution);
                            colContribIndex++;
                        }
                    }
                }
                
                IntVar[] colContributions = colContributionsList.toArray(new IntVar[0]);
                model.sum(colContributions, "=", sumOfDistancesOfElt1Elt2InAllColumns).post();

                // Post new constraint so that distanceSumElt1Elt2InAllLines = sumOfDistancesOfElt1Elt2InAllColumns
                model.arithm(distanceSumElt1Elt2InAllLines, "=", sumOfDistancesOfElt1Elt2InAllColumns).post();
                
                // We only add distanceSumElt1Elt2InAllLines to allDistanceSums since distanceSumElt1Elt2InAllLines and
                // sumOfDistancesOfElt1Elt2InAllColumns are equal
                allDistanceSums.add(distanceSumElt1Elt2InAllLines);
            }
        }
        
        // Post new constraint so that all distance sums in allDistanceSums are equal
        for (int i = 1; i < allDistanceSums.size(); i++) {
            model.arithm(allDistanceSums.getFirst(), "=", allDistanceSums.get(i)).post();
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
