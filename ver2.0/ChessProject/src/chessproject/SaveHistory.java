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
			String[] lines = new String[GameData.NUM_OF_HISTORY+1]; // ####�⺸ ���� �� ���� ����
			int line_count = 0;
			while( (lines[line_count] = br1.readLine()) != null && line_count < GameData.NUM_OF_HISTORY ) { // ####�⺸ ���� �� ���� ����
				line_count++;
			}
			br1.close();
			
			Writer bw =new BufferedWriter (new FileWriter(path));
			for(int i = 0; i < line_count; i++) {
				bw.write(lines[i]);
				bw.write('\n');
			}
			bw.write( (line_count+1) + " "); // �ش� �⺸ �̸� ����. �ϴ� 1 �̶� �ϰ� ����.
			
			int count = 0;
			int size = _history_chesslocations.size();
			while( count < size ) {
				ChessLocation _chesslocation = _history_chesslocations.get(count);
				for(int i=0; i<_chesslocation.chessboard.length; i++) { // this.chessboard�� �ش� ���� �� ��ġ ��� ����
	                this.chessboard[i] = _chesslocation.chessboard[i];
	                bw.write( Integer.toString( _chesslocation.chessboard[i]) );
	                bw.write(" "); // ���� ������ ���� ���� ����!
	                bw.flush();
	            }
				count++;
				
			}
			bw.write('\n'); // �⺸���� '\n'���� ����.
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
