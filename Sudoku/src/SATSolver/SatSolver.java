package SATSolver;

public class SatSolver {

	private Formula formula;

	public void readFormula(StringBuffer formulaString) {
            formula = new Formula(formulaString);
    }

	public boolean hasEmptyClause() {
        return formula.hasEmptyClause();
    }

	public boolean isEmpty() {
        return formula.isEmpty();
    }

	public int selectBranchVar() {
        return formula.getHighestRankVariable();
    }

	public void setVar(int var) {
        formula.setVariable(var);
    }

	public void unset() {
        formula.undoPropegate();
    }

	public void success() {
        System.out.println("Formula is satisfiable");
    }

	public void failure() {

        System.out.println("Formula is unsatisfiable");
    }

	public int[] solve(StringBuffer formulaString) {
        readFormula(formulaString);

        if (DPLL()) {
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
	public boolean DPLL() {

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

            if (DPLL()) {
                return true;

            } else {

                // Unset var in the formula
                // Undoes any unit propagation
                unset();

                // Try reversing the assignment
                setVar(-var);

                if (DPLL()) {
                    return true;
                } else {
                    unset();
                    return false;
                }
            }
        }
    }
}