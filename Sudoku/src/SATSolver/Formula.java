package SATSolver;


import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;

public class Formula {
	//Variable assignments
	private static final int TRUE = 1;
	private static final int FALSE = 0;
	private static final int UNASSIGNED = -1;
	
	// Holds the value of each variable
	public int[] currentAssign;
	public Stack<LinkedList<Integer>> currentFormula = new Stack<LinkedList<Integer>>();
	private int[][] formula; 
	private boolean hasEmptyClause;
	
	// Compute Ranks using the Jeroslow-Wang heuristic.
	private static final double[] POWERS_OF_TWO = { 0, 0.5, 0.25, 0.125, 0.625,
		0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125, 0.0009765625 };
	private double[] variableRanks;
	private int highestRank;
	private double highestRankValue;
	
	// Holds the unit variables. 
	private TreeSet<Integer> unitVariables = new TreeSet<Integer>();
	// affected by unit propagation
	private Stack<LinkedList<Integer>> variablesAffected;

	

    public Formula(StringBuffer formulaString){
        LinkedList<Integer> originalFormula = new LinkedList<Integer>();
        variablesAffected = new Stack<LinkedList<Integer>>();
        unitVariables = new TreeSet<Integer>();

        // Current clause
        int varTotal;
        int clauseCount = 0;
        int clauseTotal = 0;
        String comment = null;
        String formulaType = "";
        
        
        Scanner sc = new Scanner(formulaString.toString());
        String temp = "";
       
        while (sc.hasNextLine()) {
           temp = sc.nextLine();
           if (temp.startsWith("p")) {

                String[] parse;

                parse = temp.trim().split("\\s+");
                formulaType = parse[1];  
                
                varTotal = Integer.parseInt(parse[2]);                
                variableRanks = new double[2 * varTotal + 1];

                // initialize currentAssignment as default value -1(unsigned) 
                currentAssign = new int[varTotal + 1];
                for (int i = 1; i < currentAssign.length; i++) {
                    currentAssign[i] = UNASSIGNED;
                }
                
                clauseTotal = Integer.parseInt(parse[3]);
                formula = new int[clauseTotal][];

            }
           else {
                if (clauseCount != clauseTotal) {

                    String[] parse;
                    parse = temp.trim().split("\\s+");//read in clause

                    int varInClause = parse.length - 1;

                    originalFormula.add(clauseCount);

                    // set the number of literals for this clause
                    formula[clauseCount] = new int[varInClause];

                   
                    for (int i = 0; i < varInClause; i++) {
                        formula[clauseCount][i] = Integer.parseInt(parse[i]);
                    }
                    clauseCount++;
                }
            }
        }
        currentFormula.push(originalFormula);
        variablesAffected.add(new LinkedList<Integer>());
        hasEmptyClause = false;       
    }

  
    private void unitPropegate() {
        LinkedList<Integer> newFormula = currentFormula.peek();
        LinkedList<Integer> variablesSetting = new LinkedList<Integer>();        
        LinkedList<Integer> curFormula = new LinkedList<Integer>();

        while (unitVariables.size() > 0) {
            int unitVar = unitVariables.first();
            unitVariables.remove(unitVariables.first());
            variablesSetting.add(unitVar);

            // make sure variable is not assigned already to create contradiction           
            if (currentAssign[abs(unitVar)] != UNASSIGNED) {
                hasEmptyClause = true;
                currentFormula.push(newFormula);
                variablesAffected.push(variablesSetting);
                return;
            }

            // check if propagation made the formula empty out
            if (newFormula.size() == 0) {
                currentFormula.push(newFormula);
                variablesAffected.push(variablesSetting);
                return;
            }

            // assign the variable 
            if (unitVar < 0) {
                currentAssign[-unitVar] = FALSE;
            } else {
                currentAssign[unitVar] = TRUE;
            }

            for (Integer curClause : newFormula) {
                boolean clauseRemoved = false;
                boolean allVariablesAssigned = true;
                int trueClauseSize = 0;

                // Check each variable in the clause
                for (int j = 0; j < formula[curClause].length; j++) {
                    int variableToCheck = formula[curClause][j];
                    
                    if ((variableToCheck > 0 && currentAssign[variableToCheck] == TRUE)
                            || (variableToCheck < 0 && currentAssign[-variableToCheck] == FALSE)) {

                        //already satisfied, remove the clause
                        clauseRemoved = true;
                        break;
                    }
                    else if ((variableToCheck > 0 && currentAssign[variableToCheck] == UNASSIGNED)
                            || (variableToCheck < 0 && currentAssign[-variableToCheck] == UNASSIGNED)) {
                        trueClauseSize++;
                        allVariablesAssigned = false;
                    }
                }

                // empty clause
                if (!clauseRemoved && allVariablesAssigned) {
                    hasEmptyClause = true;
                    currentFormula.push(newFormula);
                    variablesAffected.push(variablesSetting);
                    return;
                }

                
                if (!clauseRemoved) {
                    curFormula.add(curClause);
                    // check if clause has a unit variable and add to unitVar set
                    if (trueClauseSize == 1) {
                        for (int i = 0; i < formula[curClause].length; i++) {
                            if (currentAssign[abs(formula[curClause][i])] == UNASSIGNED) {
                            	// check if a contradiction appears in the set already                                
                                if (unitVariables.contains(-formula[curClause][i])) {
                                    hasEmptyClause = true;
                                    currentFormula.push(newFormula);
                                    variablesAffected.push(variablesSetting);
                                    return;
                                } else {
                                    unitVariables.add(formula[curClause][i]);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            newFormula = curFormula;
            curFormula = new LinkedList<Integer>();
        }
        
        hasEmptyClause = false;
        currentFormula.push(newFormula);
        variablesAffected.push(variablesSetting);
    }
    
    public void undoPropegate() {
        if (currentFormula.size() > 1) {
            currentFormula.pop();
        }
        if (variablesAffected.size() > 1) {
            LinkedList<Integer> temp = variablesAffected.pop();
            for (Integer i : temp) {
                currentAssign[abs(i)] = UNASSIGNED;
            }
        }
        unitVariables.clear();
    }
    
    
    //computes the ranks for the clauses using the Jeroslow-Wang heuristic  
    private void computeRanks() {
        resetRanks();
        double tempNewHighestRankValue = 0;

        int trueClauseSize;

        for (Integer clause : currentFormula.peek()) {
            trueClauseSize = 0;
            for (int i = 0; i < formula[clause].length; i++) {
                int varToCheck = formula[clause][i];
                if (currentAssign[abs(varToCheck)] == UNASSIGNED) {
                    trueClauseSize++;
                }
            }

            //caculate the sum of 2^(-length)
            for (int i = 0; i < formula[clause].length; i++) {                
                int tempVar = formula[clause][i];
                if (tempVar > 0 && (currentAssign[tempVar] == UNASSIGNED)) {                    
                    if (trueClauseSize < 10) {
                        variableRanks[2 * tempVar - 1] += POWERS_OF_TWO[trueClauseSize];
                    } else {
                        variableRanks[2 * tempVar - 1] += Math.pow(2, -trueClauseSize);
                    }
                } 
                else if (tempVar < 0 && currentAssign[tempVar * -1] == UNASSIGNED) {
                    if (trueClauseSize < 10) {
                        variableRanks[2 * -tempVar] += POWERS_OF_TWO[trueClauseSize];
                    } else {
                        variableRanks[2 * -tempVar] += Math.pow(2, -trueClauseSize);
                    }
                }

                //take max of sum of two-side
                if (tempVar > 0) {
                    tempNewHighestRankValue = variableRanks[2 * tempVar - 1]
                            + variableRanks[2 * tempVar];
                } else {
                    tempVar = -tempVar;
                    tempNewHighestRankValue = variableRanks[2 * (tempVar) - 1]
                            + variableRanks[2 * (tempVar)];
                }

                if (tempNewHighestRankValue > highestRankValue) {
                    if (variableRanks[2 * tempVar - 1] > variableRanks[2 * tempVar]) {
                        highestRank = tempVar;
                        highestRankValue = tempNewHighestRankValue;
                    } else {
                        highestRank = -tempVar;
                        highestRankValue = tempNewHighestRankValue;
                    }
                }
            }
        }
    }

    
    private void resetRanks() {
        variableRanks[0] = -1;
        for (int i = 1; i < variableRanks.length; i++) {
            variableRanks[i] = 0;
        }
        highestRank = -1;
        highestRankValue = -1;
    }

    
    public int getHighestRankVariable() {
        computeRanks();
        return highestRank;
    }

    
    public boolean hasEmptyClause() {
        return hasEmptyClause;
    }

   
    public void setVariable(int var) {
        if (unitVariables.size() == 0) {
            unitVariables.add(var);
        }
        unitPropegate();
    }
    
    public boolean isEmpty() {
        return currentFormula.peek().size() == 0;
    }

    private int abs(int num) {
        if (num < 0) {
            return -num;
        }
        return num;
    }

    public int[] getCurrentAssigment() {
        return currentAssign;
    }

   //print solution
    @Override
    public String toString() {
        String temp = "";
        for (int i = 1; i < currentAssign.length; i++) {
            temp += "Variable " + i + " Value " + currentAssign[i] + "\n";
        }
        return temp;
    }
}
