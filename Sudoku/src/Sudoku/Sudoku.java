package Sudoku;

import java.io.IOException;

import FileIO.ReadInFile;
import SATSolver.Formula;
import SATSolver.SatSolver;

public class Sudoku {
    private static final int CLAUSE_COUNT = 11988;
    private final static int SQUARE_SIZE = 9;
    private final int NUM_VARS = 9;
    private static int totalClauseCount =0;
    
	public static void main(String[] arg){
		ReadInFile rf =  ReadInFile.getInstance();
		try{
			char[][] in = rf.readFile("src/InputFile/input_sudoku.txt");
			for(int i = 0; i < 9; i ++){
				int row = i+1;
				System.out.print("row #"+ row +": ");
				for(int j = 0; j < 9; j ++)	{
					System.out.print(in[i][j] + " ");
				}
				System.out.println();
			}
			String tmpLine = "1 2 2 0";
			String[] parse = tmpLine.trim().split("\\s+");
			for(String s : parse){
				System.out.print(s + "+");
			}
			MapperDefault se= new MapperDefault();	
			String out = se.out.toString();
			StringBuffer combinedConstaint = new StringBuffer();
			StringBuffer s = mapper(in);
			combinedConstaint.append("p cnf 999 " +totalClauseCount+ "\n");
			combinedConstaint.append(mapper(in).append(se.out));
			System.out.println("##########-------------##############");
			System.out.println(combinedConstaint.toString().substring(0,1000));
			System.out.println("##########-------------##############");

			SatSolver ss = new SatSolver();
			//ss.readFormula(combinedConstaint);
			ss.solve(combinedConstaint);
		}catch(IOException e){
			e.printStackTrace();
		}	
	}
	
	public static StringBuffer mapper(char[][] board){
		StringBuffer sb = new StringBuffer();
        int clauseCount = 0;

		for(int row = 0; row < SQUARE_SIZE; row++){
			for(int col = 0; col < SQUARE_SIZE; col ++ ){
				if(board[row][col] != '0'){
					int temRow = row +1;
					int temCol = col +1;
					sb.append(temRow+ "" +temCol+ ""+ board[row][col] + " 0\n");
					clauseCount++;
				}
			}
		}
		totalClauseCount = clauseCount + CLAUSE_COUNT;

		return sb;
	}
}
