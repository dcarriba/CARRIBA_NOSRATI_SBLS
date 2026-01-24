package com.dcarriba.sbls;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.util.tools.ArrayUtils;

/**
 * The advanced implementation of the {@link SpatiallyBalancedLatinSquare} problem.
 */
public class AdvancedSpatiallyBalancedLatinSquare extends SpatiallyBalancedLatinSquare{

    /**
     * Constructor of the {@link AdvancedSpatiallyBalancedLatinSquare} class
     *
     * @param n             size of the Spatially Balanced Latin Square
     * @param printSolution if the solution (if found) should be printed
     */
    public AdvancedSpatiallyBalancedLatinSquare(int n, boolean printSolution) {
        super(n, printSolution);
    }

    @Override
    protected void instantiateProblem() {
        super.setModel(new Model(super.getN() + "*" + super.getN() + " Advanced Spatially Balanced Latin Square"));
        super.setVariables(super.getModel().intVarMatrix(super.getN(), super.getN(), 0, super.getN()-1));

        postLineAllDiffConstraint("AC");
        postColumnAllDiffConstraint("AC");
        postSpatiallyBalancedConstraint();

        super.setSolver(super.getModel().getSolver());

        configureSearchStrategy();
        addSymmetryBreaking();

        System.out.println(super.getModel().getName() + " problem has been instantiated.\n");
    }

    /**
     * Configures the search strategy used to solve the SBLS problem.
     * The Domain over Weighted Degree search strategy is used.
     */
    private void configureSearchStrategy() {
        super.getSolver().setSearch(
            Search.domOverWDegSearch(ArrayUtils.flatten(super.getVariables()))
        );        
    }

    /**
     * Adds symmetry breaking constraints to reduce the search space.
     */
    private void addSymmetryBreaking() {
        for (int i = 0; i < super.getN(); i++) {
            super.getModel().arithm(super.getVariables()[0][i], "=", super.getVariables()[i][0]).post();
        }
    }
}
