package FileIO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadInFile {
	private static ReadInFile instance;
	
    private final int SQUARE_SIZE = 9;
    private final int NUM_VARS = 9;
    
	char[][] initialBoard = new char[SQUARE_SIZE][SQUARE_SIZE];
	
	public static ReadInFile getInstance(){
		if(instance == null)
			instance = new ReadInFile();
		return instance;
	}
	
	public char[][] readFile(String filePath) throws IOException{
		FileInputStream fstream = new FileInputStream(filePath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		int row = 0;
		String[] parse = new String[SQUARE_SIZE];
		String strLine;
		while ((strLine = br.readLine()) != null){
			String s = strLine;
			if(s.equals("")){
				break;
			}else{
				char[] thisRow = s.toCharArray();
				for(int i = 0, j = 0; i < s.length(); i+=2, j++){
					if(isValid(thisRow[i])){
						initialBoard[row][j] = thisRow[i];
					}else{
						return null;
					}
				}
				row ++;
			}
		}
		return initialBoard;
	}
	
	public boolean isValid(char c){
		if(c < 48 || c > 57 )
			return false;
		return true;
	}
}
