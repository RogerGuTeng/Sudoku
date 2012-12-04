package SATSolver;

/**
 * This class is the main Davis-Putnam algorithm.  The algorithm
 * is implemented recursively and uses the associated Formula
 * class to represent the boolean formula to be solved.
 *
 * A formula is empty if all of the clauses are satisfied with
 * the current assignment.
 *
 * A clause is empty if all the variables are assign and it is
 * not satisfied.
 *
 * When setting variables in a formula the values are assumed as
 * the following:
 * 		false:       0
 * 		true:        1
 * 		unassigned: -1
 *
 * Variables chosen based on the Jeroslow-Wang heuristic.
 *
 * May 2009
 * @author Dr. Baliga, Paul Varoutsos, Tom Devito
 *
 */

import java.io.FileNotFoundException;

public class SatSolver {

public Formula formula;

    /**
     * Reads in the formula and creates a formula object
     *
     * @param filename - The file path that holds the
     *                   file containing the formula to solve.
     */
public void readFormula(StringBuffer formulaString) {

            formula = new Formula(formulaString);
    }

    /**
     * @return - Returns true if the formula has at least one
     *           clause  that is currently not satisfiable with
     * 		 the current assignment.
     * 		 Returns false otherwise.
     */
public boolean hasEmptyClause() {

        return formula.hasEmptyClause();
    }

    /**
     * 	This method determines if the formula is empty.  It
     * 	will be empty if the current formula has no more
     *  unsatisfiable clauses.  The current formula will be
     *  size 0.
     *
     * @return - Returns true if the formula is satisfied
     *           Returns false otherwise.
     */
public boolean isEmpty() {

        return formula.isEmpty();
    }

    /**
     * Selects the next variable to branch to.  This will select
     * the variable with the highest ranking.  The branch variable
     * is selected using the Jeroslow-Wang heuristic.
     *
     * @return - The variable with the highest ranking and the one
     * 		 that will be branched to next.
     */
public int selectBranchVar() {

        return formula.getHighestRankVariable();
    }

    /**
     * Sets a variable to a value.
     * After a variable is set, the formula is checked and
     * true clauses are removed.
     *
     * @param var - The variable to set
     */
public void setVar(int var) {

        formula.setVariable(var);
    }

    /**
     * Unsets a variable to an unsigned value and undoes
     * any unit propagation that was done as a result of
     * setting that  variable.
     */
public void unset() {

        formula.undoProp();
    }

    /**
     * This method controls what to do in the case that
     * the formula was satisfiable.
     * Currently prints out the satisfying assignment.
     */
public void success() {

        System.out.println("Formula is satisfiable");
        System.out.println(formula.toString());
    }

    /**
     * This method controls what to do in the case that the
     * formula was not satisfiable.
     */
public void failure() {

        System.out.println("Formula is unsatisfiable");
    }

    /**
     *	This is the starting point of the DP algorithm.
     *
     * @param fname - The filename that contains the formula to solve.
     */
public int[] solve(StringBuffer formulaString) {

        readFormula(formulaString);

        if (dp()) {
            success();
            return formula.getCurrentAssigment();

        } else {
            failure();
            return null;
        }
    }

    /**
     * This is the recursive method that performs the Davis-Putnam
     * algorithm.
     * @return - True if a formula is empty (satisfiable)
     * 		   - False if a formula has an empty clause.
     */
public boolean dp() {

        //If the formula is empty, no more clauses need to be satisfied
        if (isEmpty()) {
            return true;

            //If a clause exists that cannot be satisfied with
            //the current assignment
        } else if (hasEmptyClause()) {
            return false;

        } else {

            int var = selectBranchVar();

            //compute ranks will give the branch variable and
            //unitProp will give the assignment.
            setVar(var);

            if (dp()) {
                return true;

            } else {

                // Unset var in the formula
                // Undoes any unit propagation
                unset();

                // Try reversing the assignment
                setVar(-var);

                if (dp()) {
                    return true;
                } else {
                    unset();
                    return false;
                }
            }
        }
    }
}