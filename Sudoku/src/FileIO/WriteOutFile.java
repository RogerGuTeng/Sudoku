package FileIO;

public class WriteOutFile {
	private static WriteOutFile instance;
	private int[][] result= null;
	
	public static WriteOutFile getInstance(){
		if(instance == null){
			instance = new WriteOutFile();
		}
		return instance;
	}
	
	public int[][] parseResult(int[] result, int length){
		this.result = new int[length][length];
		int row = 0;
		int col = 0;
		if(result == null){
			return null;
		}
		for(int i = 111; i < 1000; i ++){
			if(result[i] == 1){
				this.result[row][col] = i%10;
				if(col<8){
					col++;
				}else{
					row ++;
					col = 0;
				}
			}
		}	
		return this.result;
	}
}
