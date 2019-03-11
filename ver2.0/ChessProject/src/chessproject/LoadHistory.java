package chessproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadHistory {
	public String path;
	public List<ChessLocation> history_chesslocations = new ArrayList<ChessLocation>();
	public int[] chessboard;
	
	public LoadHistory() {
		String now_path = System.getProperty("user.dir");
		path = new String(now_path + "/save/histories.txt");
		chessboard = new int[120];
	}
	
	public List<ChessLocation> load(int num_h, List<ChessLocation> _history_chesslocations, ChessProject chessproject) { // 로드할 라인 있으면 true, 없으면 false 반환
		try {
			
			int num, index;
			String line = "";
			BufferedReader br = new BufferedReader (new FileReader(path));
			if( (line = br.readLine()) == null ) {
				chessproject.showNoHistoryWarning();
				return _history_chesslocations;
			}
			try {
			while (Integer.parseInt(line.substring(0, 1)) != num_h && line != null) {
				line = br.readLine();
			}
			}
			catch (Exception e) {
				chessproject.history_button_pane.setVisible(true);
			}
			if(line == null) {
				chessproject.showNoHistoryWarning();
				return _history_chesslocations;
			}
			
			String[] tempArray = line.split(" ");
			
			_history_chesslocations.clear(); // 받는 히스토리 체스로케이션 일단 비움!
			num = (tempArray.length -1) / 120;
			index = 1;
			for(int i = 0; i < num; i++) {
				ChessLocation _chesslocation = new ChessLocation();
				_chesslocation.initialize(true); //인간이 하양일 때만!!
				for(int j = 0; j < 120; j++) {
					_chesslocation.chessboard[j] = Integer.parseInt(tempArray[index++]);
				}
				history_chesslocations.add( new ChessLocation(_chesslocation) );
			}
			index = 1;
			
			
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return history_chesslocations;
	}
	
	
}
