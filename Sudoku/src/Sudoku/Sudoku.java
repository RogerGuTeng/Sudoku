package Sudoku;

import java.io.IOException;

import FileIO.ReadInFile;

public class Sudoku {
	public static void main(String[] arg){
		ReadInFile rf =  ReadInFile.getInstance();
		try{
			char[][] in = rf.readFile("src/InputFile/input_sudoku.txt");
			for(int i = 0; i < 9; i ++){
				System.out.print("row #"+ i +": ");
				for(int j = 0; j < 9; j ++)	{
					System.out.print(in[i][j] + " ");
				}
				System.out.println();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
