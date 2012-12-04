package Sudoku;
import java.io.IOException;
public class SATEncode {
	private final int NUM_VARS =9;
	private final int SQUARE_SIZE =9;
	public StringBuffer out = new StringBuffer();
	public SATEncode(){
		try{
			this.GenerateClauses();
		}catch(IOException e){
			e.printStackTrace();
		}

	}
	//check that each number must occur at least once in the row
	private void row_least_once() throws IOException{
		for(int val=1;val<=NUM_VARS;val++){
			for(int row=1;row<=SQUARE_SIZE;row++){
				for(int col=1;col<=SQUARE_SIZE;col++){
					out.append(row+""+col+""+val+" ");
				}
				out.append("0\n");
			}
		}
	}
	
	//check that each number music occur only once in the row
	private void row_only_once() throws IOException{
		for(int val =1; val<NUM_VARS;val++){
			for(int row=1;row<SQUARE_SIZE;row++){
				for(int col=1;col<=SQUARE_SIZE;col++){
					int after =1;
					for(int i=col;i<SQUARE_SIZE;i++){
						out.append("-"+row+""+col+""+val+" ");
						out.append("-"+row+""+(col+after)+""+val+" 0\n");
						after++;
					}
				}
			}
		}
	}
	
	//check that each number must occur at least once in the column
	private void col_least_once() throws IOException{
		for (int val = 1; val <= NUM_VARS; val++) {
            for (int col = 1; col <= SQUARE_SIZE; col++) {
                for (int row = 1; row <= SQUARE_SIZE; row++) {
                    out.append(row + "" + col + "" + val + " ");
                }
                out.append("0\n");
            }
        }
	}
	
	//check that each number must occur only once in the column
	private void col_only_once() throws IOException{
		for (int val = 1; val <= NUM_VARS; val++) {
            for (int col = 1; col <= SQUARE_SIZE; col++) {
                for (int row = 1; row <= SQUARE_SIZE; row++) {
                    int after = 1;
                    for (int i = row; i < SQUARE_SIZE; i++) {
                        out.append("-" + row + "" + col + "" + val + " ");
                        out.append("-" + (row + after) + "" + col + "" + val + " 0\n");
                        after++;
                    }
                }
            }
        }
	}
	
	//each number music occur at least once in each sub box.
	 private void box_least_once() throws IOException {

	        for (int val = 1; val <= NUM_VARS; val++) {
	            for (int row = 1; row <= SQUARE_SIZE; row += 3) {
	                for (int col = 1; col <= SQUARE_SIZE; col += 3) {

	                    for (int i = 0; i < 3; i++) {
	                        for (int j = 0; j < 3; j++) {
	                            out.append((row + i) + "" + (col + j) + "" + val + " ");
	                        }
	                    }
	                    out.append("0\n");
	                }
	            }
	        }
	    }
	 
	 private void box_only_once() throws IOException {
	        for (int val = 1; val <= NUM_VARS; val++) {
	            for (int row = 1; row <= SQUARE_SIZE; row += 3) {
	                for (int col = 1; col <= SQUARE_SIZE; col += 3) {

	                    int index = 0;
	                    
	                    String[] array = new String[9];

	                    for (int i = 0; i < 3; i++) {
	                        for (int j = 0; j < 3; j++) {
	                            array[index] = (row + i) + "" + (col + j) + "" + val;
	                            index++;
	                        }
	                    }

	                    //same as row_only_once	                   
	                    for (int j = 0; j <= array.length; j++) {
	                        int after = 1;
	                        for (int i = j; i < array.length - 1; i++) {
	                            out.append("-" + array[j] + " ");
	                            out.append("-" + array[j + after] + " 0\n");
	                            after++;
	                        }
	                    }
	                }
	            }
	        }
	    }
	 //each unit has at least one number
	 private void unit_least_once() throws IOException {

	        for (int row = 1; row <= SQUARE_SIZE; row++) {
	            for (int col = 1; col <= SQUARE_SIZE; col++) {
	                for (int val = 1; val <= SQUARE_SIZE; val++) {
	                    out.append(row + "" + col + "" + val + " ");
	                }
	                out.append("0\n");
	            }
	        }
	 }
	 //each unit has only one number
	 private void unit_only_once() throws IOException {
	        for (int row = 1; row <= SQUARE_SIZE; row++) {
	            for (int col = 1; col <= SQUARE_SIZE; col++) {
	                for (int val = 1; val <= NUM_VARS; val++) {
	                    for (int next = val + 1; next <= SQUARE_SIZE; next++) {
	                        out.append("-" + row + "" + col + "" + val + " ");
	                        out.append("-" + row + "" + col + "" + next + " 0\n");
	                    }
	                }
	            }
	        }
	    }
	 public void GenerateClauses() throws IOException {	        
	        row_least_once();
	        row_only_once();
	        col_least_once();
	        col_only_once();
	        box_least_once();
	        box_only_once();
	        unit_least_once();
	        unit_only_once();
	 }
	 
	 
	 //for testing
	 /*public static void main(String[] args){
		 try{
		 StringBuffer out= new StringBuffer();
		 SATEncode se= new SATEncode(out);		 
			 se.GenerateClauses();
			 String result = se.out.toString();
			 System.out.print(result);
		 }catch(IOException e){
			 e.printStackTrace();
		 }
		 
	 }*/
}
