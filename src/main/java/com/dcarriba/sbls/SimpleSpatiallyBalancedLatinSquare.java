package com.dcarriba.sbls;

import org.chocosolver.solver.Model;

/**
 * The simple implementation of the {@link SpatiallyBalancedLatinSquare} problem.
 */
public class SimpleSpatiallyBalancedLatinSquare extends SpatiallyBalancedLatinSquare {

    /**
     * Constructor of the {@link SimpleSpatiallyBalancedLatinSquare} class
     *
     * @param n             size of the Spatially Balanced Latin Square
     * @param printSolution if the solution (if found) should be printed
     */
    public SimpleSpatiallyBalancedLatinSquare(int n, boolean printSolution) {
        super(n, printSolution);
    }

    @Override
    protected void instantiateProblem() {
        super.setModel(new Model(super.getN() + "*" + super.getN() + " Simple Spatially Balanced Latin Square"));
        super.setVariables(super.getModel().intVarMatrix(super.getN(), super.getN(), 0, super.getN()-1));

        postLineAllDiffConstraint("DEFAULT");
        postColumnAllDiffConstraint("DEFAULT");
        postSpatiallyBalancedConstraint();

        super.setSolver(super.getModel().getSolver());

        System.out.println(super.getModel().getName() + " problem has been instantiated.\n");
    }
}
