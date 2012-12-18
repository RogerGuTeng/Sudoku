package Sudoku;

import java.io.IOException;

import FileIO.ReadInFile;
import FileIO.WriteOutFile;
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

			
			SATEncode se= new SATEncode();	
			String out = se.out.toString();
			StringBuffer combinedConstaint = new StringBuffer();
			StringBuffer s = mapper(in);
			combinedConstaint.append("p cnf 999 " +totalClauseCount+ "\n");
			combinedConstaint.append(mapper(in).append(se.out));
			//System.out.println("##########-------------##############");
			//System.out.println(combinedConstaint.toString().substring(0,1000));
			//System.out.println("##########-------------##############");

			SatSolver ss = new SatSolver();
			//ss.readFormula(combinedConstaint);
			int[] array = ss.solve(combinedConstaint);
			int[][] result = new int[SQUARE_SIZE][SQUARE_SIZE];
			
			WriteOutFile wf = WriteOutFile.getInstance();
			if(array != null){
				result = wf.parseResult(array, SQUARE_SIZE);
				for(int i = 0; i < SQUARE_SIZE; i++){
					if(i == 0){
						for(int d = 0; d < SQUARE_SIZE; d++){
							if((d+1)%3==0){
								System.out.print("=====");							
							}else{
								System.out.print("====");
							}
						}
						System.out.println();
					}
					for(int j = 0; j < SQUARE_SIZE; j ++){
						if(j == 0){
							System.out.print("[ ");
						}
						if((j+1)%3==0 && j != 8){
							System.out.print(result[i][j] + " ][ ");
						}else if(j == 8){
							System.out.print(result[i][j] + " ] ");
						}else{
							System.out.print(result[i][j] + " | ");
						}
					}
					System.out.println();
					for(int d = 0; d < SQUARE_SIZE; d++){
						if((i+1)%3==0){
							if((d+1)%3==0){
								System.out.print("=====");							
							}else{
								System.out.print("====");
							}
						}else{
							if((d+1)%3==0){
								System.out.print("-----");							
							}else{
								System.out.print("----");
							}
						}
					}
					System.out.println();
				}
			}
			
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
