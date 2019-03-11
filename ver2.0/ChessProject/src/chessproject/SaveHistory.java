package chessproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveHistory {
	public String path;
	public List<ChessLocation> history_chesslocations = new ArrayList<ChessLocation>();
	public int[] chessboard;
	
	public SaveHistory() {
		String now_path = System.getProperty("user.dir");
		path = new String(now_path + "/save/histories.txt");
		chessboard = new int[120];
	}
	
	public void save( List<ChessLocation> _history_chesslocations) {
		try {
			BufferedReader br1 = new BufferedReader (new FileReader(path));
			String[] lines = new String[GameData.NUM_OF_HISTORY+1]; // ####기보 저장 수 변경 가능
			int line_count = 0;
			while( (lines[line_count] = br1.readLine()) != null && line_count < GameData.NUM_OF_HISTORY ) { // ####기보 저장 수 변경 가능
				line_count++;
			}
			br1.close();
			
			Writer bw =new BufferedWriter (new FileWriter(path));
			for(int i = 0; i < line_count; i++) {
				bw.write(lines[i]);
				bw.write('\n');
			}
			bw.write( (line_count+1) + " "); // 해당 기보 이름 저장. 일단 1 이라 하고 시작.
			
			int count = 0;
			int size = _history_chesslocations.size();
			while( count < size ) {
				ChessLocation _chesslocation = _history_chesslocations.get(count);
				for(int i=0; i<_chesslocation.chessboard.length; i++) { // this.chessboard에 해당 씬의 말 위치 모두 저장
	                this.chessboard[i] = _chesslocation.chessboard[i];
	                bw.write( Integer.toString( _chesslocation.chessboard[i]) );
	                bw.write(" "); // 사이 간격을 뭐로 나눌 건지!
	                bw.flush();
	            }
				count++;
				
			}
			bw.write('\n'); // 기보들을 '\n'으로 나눔.
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
