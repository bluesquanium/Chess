/* check(1)
 * 프로그램 실행되는 제일 중심 파일.
 * 
 */
package chessproject;

import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;


public class ChessProject extends JFrame implements MouseListener{
	//history 관련 추가
	SaveHistory history_saver = new SaveHistory();
	LoadHistory history_loader = new LoadHistory();
	HistoryPane history_button_pane;
	//network 관련 추가
	NetworkPane network_options;
	NetworkMessengerHost messengerHost;
	NetworkMessengerClient messengerClient;
	NetworkGameHost networkgameHost;
	NetworkGameClient networkgameClient;
	String opponent_address;
	boolean send = false;
	String sendingM; // 보낼 메시지 저장
	
	
    ChessLocation chesslocation;
    ChessBoardPane board_pane;
    HistoryBoardPane history_pane;
    JPanel east_pane;
    Resource resource = new Resource();
    Map<Integer,Image> images = new HashMap<Integer,Image>();
    Map<Integer,Icon> icon_images = new HashMap<Integer,Icon>();
    Move move = new Move();
    boolean piece_selected;
    boolean is_white; // 플레이어 말 색깔 뭔지 알려줌. 그 게임이 끝날 때까진 안 변함
    int gametype; // newgame = 0, local = 1, network = 2 
    int state;
    Game game;
    JLabel local_game, network_game, quit, about, history, first, prev, next, last;
    JPanel main_pane = new JPanel(new BorderLayout());
    boolean castling;
    PromotionPane promotion_pane;
    List<ChessLocation> history_chesslocations = new ArrayList<ChessLocation>();
    int history_count;
    Color bg_color = Color.decode("#efd39c"); // 전체 화면의 배경색.
    
    public ChessProject(){
        super("ChessProject "+GameData.VERSION); // 화면 제목                                
        setContentPane(main_pane);  // 메인페인 넣는거 같은데 정확히는 모르겠다. ##########
        chesslocation = new ChessLocation();
        promotion_pane = new PromotionPane(this);
        
        loadMenuIcons();
        loadBoardImages();
        
        board_pane = new ChessBoardPane(); // 보드부분 페인 만들기
        
        main_pane.add(createMenuPane(),BorderLayout.WEST); // 메뉴페인 만들고 왼쪽에 넣기
        main_pane.add(board_pane,BorderLayout.CENTER);   // 보드페인 가운데 넣기
        main_pane.setBackground(bg_color);      
        createEastPane(); // 오른쪽 페인 만들기
        east_pane.setVisible(true);
        
        pack();
        Dimension size = getSize();
        size.height = 523; // 전체 프레임 높이 지정
        setSize(size);
        
        addWindowListener(new WindowAdapter(){ // 윈도우 닫는지 확인하는듯
            public void windowClosing(WindowEvent e){
                quit();
            }
        });
    }               
    public JPanel createMenuPane(){ // 메뉴 페인에 관한 내용
        local_game = new JLabel(icon_images.get(GameData.BUTTON_LOCALGAME));
        network_game = new JLabel(icon_images.get(GameData.BUTTON_NETWORKGAME));
        about = new JLabel(icon_images.get(GameData.BUTTON_ABOUT));
        history = new JLabel(icon_images.get(GameData.BUTTON_HISTORY));
        quit = new JLabel(icon_images.get(GameData.BUTTON_QUIT));  
        
        local_game.addMouseListener(this);
        network_game.addMouseListener(this);
        about.addMouseListener(this);
        history.addMouseListener(this);
        quit.addMouseListener(this);
        
        JPanel pane = new JPanel(new GridLayout(6,1));
        pane.add(local_game);
        pane.add(network_game);
        pane.add(history);
        pane.add(about);
        pane.add(quit);             
        pane.setBackground(bg_color); // 배경색 지정.얜 근데 빼도 됨
        JPanel menu_pane = new JPanel(new BorderLayout());
        menu_pane.setBackground(bg_color);
        menu_pane.add(pane,BorderLayout.SOUTH); // 버튼들 아랫쪽에 두기
        menu_pane.setBorder(BorderFactory.createEmptyBorder(5,24,0,10)); // 상좌하우 빈칸 지정
        return menu_pane;
    }
    public void createEastPane(){ // 히스토리 페인이 들어가는 곳
        east_pane = new JPanel(new BorderLayout());
        history_pane = new HistoryBoardPane();                
        
        JPanel pane = new JPanel(new GridLayout(1,4));        
        first = new JLabel(icon_images.get(GameData.BUTTON_FIRST));
        prev = new JLabel(icon_images.get(GameData.BUTTON_PREV));
        next = new JLabel(icon_images.get(GameData.BUTTON_NEXT));
        last = new JLabel(icon_images.get(GameData.BUTTON_LAST));
        
        pane.add(first);
        pane.add(prev);
        pane.add(next);
        pane.add(last);
        
        JPanel pane2 = new JPanel();
        pane2.setLayout(new BoxLayout(pane2,BoxLayout.Y_AXIS));
        pane2.add(history_pane);
        pane2.add(pane);
        
        east_pane.add(pane2,BorderLayout.SOUTH);
        east_pane.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        east_pane.setBackground(bg_color);        
        east_pane.setVisible(false);
        main_pane.add(east_pane,BorderLayout.EAST);
        
        pane.setBorder(BorderFactory.createEmptyBorder(0,14,0,14));
        pane.setBackground(bg_color);
        
        first.addMouseListener(this);
        prev.addMouseListener(this);
        next.addMouseListener(this);
        last.addMouseListener(this);
    }
    
    public void newGameLocal(){
    	gametype = 1;
    	state = GameData.GAME_END;
    	if(!east_pane.isVisible()){
            east_pane.setVisible(true);
            pack();
            setLocationRelativeTo(null);
        }        
        is_white = true; // 아래가 흰색
        move.source_location = -1;
        move.destination = -1;
        chesslocation = new ChessLocation();
        chesslocation.initialize(is_white);
        game = new Game(chesslocation);
        loadPieceImages();
        promotion_pane.setIcons(is_white);
        board_pane.repaint();
        if(is_white) state = GameData.PLAYER1_MOVE;
        else state = GameData.PLAYER2_MOVE;
        castling = false;
        history_chesslocations.clear();
        history_count = 0;
        newHistoryChessLocation();
        playLocal();
    }
    
    public void newHistoryGame(){
    	int num_h = history_button_pane.levelSlider.getValue();
    	state = GameData.GAME_END;
        is_white = true; // 아래가 흰색
        move.source_location = -1;
        move.destination = -1;
        //chesslocation = new ChessLocation();
        //chesslocation.initialize(is_white);
        //game = new Game(chesslocation);
        loadPieceImages();
        promotion_pane.setIcons(is_white);
        board_pane.repaint();
        if(is_white) state = GameData.PLAYER1_MOVE;
        else state = GameData.PLAYER2_MOVE;
        castling = false;
    	history_chesslocations = history_loader.load(num_h, history_chesslocations, this);
        history_count = 0;
        chesslocation = new ChessLocation(); // 보드페인 지우는 용도
        board_pane.repaint();
        history_pane.repaint();
    	
    	
        /* 저장 잘 되었나 체크
        int num = 8;
        for(int i = 0; i < num; i++) {
			ChessLocation _chesslocation = history_chesslocations.get(i);
			for(int j = 0; j < 120; j++) {
				
				System.out.print(_chesslocation.chessboard[j]);

			}
			System.out.println("");
		}
        System.out.println("");
        System.out.println(history_chesslocations.size());
        */
        //newHistoryChessLocation();
    }
    
    public void newGameNetwork(){
    	if(gametype == 2) {
    		showAlreadyPlayNetworkWarning();
    		return;
    	}
    	opponent_address = new String( network_options.textField.getText() );
    	try {
        	if( network_options.host_button.isSelected() == true) {
        		messengerHost = new NetworkMessengerHost( opponent_address );
        		messengerHost.process();
        		networkgameHost = new NetworkGameHost( opponent_address );
        		networkgameHost.process();
        	}
        	else {
        		messengerClient = new NetworkMessengerClient( opponent_address );
        		messengerClient.process();
        		networkgameClient = new NetworkGameClient( opponent_address );
        		networkgameClient.process();
        	}
        } catch(IOException e) {
        	e.printStackTrace();
        	showUsingPortWarning();
        	return;
        }
    	gametype = 2;
    	state = GameData.GAME_END;
    	//is_white = network_options.host_button.isSelected(); // host면 is_white값 true로 함.
    	is_white = true; // 원래 위에걸로 하려고 했는데 쉽게 구현을 위해선 아래로 해야할듯
        move.source_location = -1;
        move.destination = -1;
        chesslocation = new ChessLocation();
        chesslocation.initialize(is_white);
        game = new Game(chesslocation);
        loadPieceImages();
        promotion_pane.setIcons(is_white);
        board_pane.repaint();
        if(is_white) state = GameData.PLAYER1_MOVE;
        else state = GameData.PLAYER2_MOVE;
        castling = false;
        history_chesslocations.clear();
        history_count = 0;
        newHistoryChessLocation();
        playNetwork();
    }
    
    public void againGameNetwork(){    	
    	gametype = 2;
    	state = GameData.GAME_END;
    	//is_white = network_options.host_button.isSelected(); // host면 is_white값 true로 함.
    	is_white = true; // 원래 위에걸로 하려고 했는데 쉽게 구현을 위해선 아래로 해야할듯
        move.source_location = -1;
        move.destination = -1;
        chesslocation = new ChessLocation();
        chesslocation.initialize(is_white);
        game = new Game(chesslocation);
        loadPieceImages();
        promotion_pane.setIcons(is_white);
        board_pane.repaint();
        if(is_white) state = GameData.PLAYER1_MOVE;
        else state = GameData.PLAYER2_MOVE;
        castling = false;
        history_chesslocations.clear();
        history_count = 0;
        newHistoryChessLocation();
        playNetwork();
    }
    
    public class NetworkGameHost {
    	public JTextField textField;
    	public JTextArea textArea;
    	public JButton button;
    	
    	DatagramSocket socket;
    	DatagramPacket packet;
    	InetAddress address = null;
    	final int myPort = GameData.NETWORKGAME_HOST_PORT;
    	final int otherPort = GameData.NETWORKGAME_CLIENT_PORT;
    	
    	public NetworkGameHost(String _opponent_address ) throws IOException {
    		address = InetAddress.getByName( _opponent_address );
    		socket = new DatagramSocket(myPort);
    	}
    	
    	public void process() {
    		MyFrame f = new MyFrame();
    		
    		Thread t = new Thread(){
    			public void run() {
    				while(true){
    					try {
    						byte[] buf = new byte[256];
    						packet = new DatagramPacket(buf, buf.length);
    						socket.receive(packet);
    						String temp = new String(buf);
    						System.out.println("Receive : " + temp); // 받는거 확인
    						String[] receive_data = temp.split(" "); // string을 " "로 나눠서 저장
    						move.source_location = Integer.parseInt(receive_data[0]); // receive_data[0]은 move.source_location임 
    						move.destination = Integer.parseInt( receive_data[1].substring(0,2) ); // receive_data[1]은 move.destination임
    						
    						state = GameData.PREPARE_MOVING; // 이건 메인 파일에서
    					} catch (SocketException socketException) {
    						break;
    					} catch (IOException ioException) {
    						ioException.printStackTrace();
    					}
    				}
    			}
    		};
    		t.start();
    	}
    	
    	public void close() {
    		socket.close();
    	}
    	
    	class MyFrame extends JFrame implements ActionListener {
    		public MyFrame() {
    			super("NetworkGameHost");
    			//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    			
    			textField = new JTextField(30);
    			textField.addActionListener(this);
    			textArea = new JTextArea(10, 30);
    			textArea.setEditable(false);
    			button = new JButton();
    			button.addActionListener(this);
    			
    			add(button, BorderLayout.NORTH);
    			add(textField, BorderLayout.PAGE_END);
    			add(textArea, BorderLayout.CENTER);
    			pack();
    			setVisible(false);
    			
    			addWindowListener(new WindowAdapter(){ // 윈도우 닫는지 확인하는듯
    	            public void windowClosing(WindowEvent e){
    	                quit();
    	            }
    	        });
    		}
    		
    		 public void quit(){
    		        int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
    		                    "NetworkMessengerHost", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 버전 수정######
    		        if(option == JOptionPane.YES_OPTION)
    		            System.exit(0);
    		        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    		 }
    		
    		public void actionPerformed(ActionEvent evt) {
    			//String s = textField.getText();
    			//byte[] buffer = s.getBytes();
    			byte[] buffer = sendingM.getBytes();
    			DatagramPacket packet;
					
    			//패킷 생성
    			packet = new DatagramPacket(buffer, buffer.length, address, otherPort);
    			try {
    				socket.send(packet);
    				System.out.println("Send : " + sendingM); // 보내는거 확인
    			}
    			catch (IOException e) {
    				e.printStackTrace();
    			}
    			textArea.append( new String(buffer) );
    			textField.setText("");
    			textArea.setCaretPosition(textArea.getDocument().getLength());
    		}
    	}
    }
    
    public class NetworkGameClient {
    	public JTextField textField;
    	public JTextArea textArea;
    	public JButton button;
    	
    	DatagramSocket socket;
    	DatagramPacket packet;
    	InetAddress address = null;
    	final int myPort = GameData.NETWORKGAME_CLIENT_PORT;
    	final int otherPort = GameData.NETWORKGAME_HOST_PORT;
    	
    	public NetworkGameClient( String _opponent_address ) throws IOException {
    		address = InetAddress.getByName( _opponent_address );
    		socket = new DatagramSocket(myPort);
    	}
    	
    	public void process() {
    		MyFrame f = new MyFrame();
    		
    		Thread t = new Thread(){
    			public void run() {
    				while(true){
    					try {
    						byte[] buf = new byte[256];
    						packet = new DatagramPacket(buf, buf.length);
    						socket.receive(packet);
    						String temp = new String(buf);
    						System.out.println("Receive : " + temp); // 받는거 확인
    						String[] receive_data = temp.split(" "); // string을 " "로 나눠서 저장
    						move.source_location = Integer.parseInt(receive_data[0]); // receive_data[0]은 move.source_location임
    						move.destination = Integer.parseInt( receive_data[1].substring(0,2) ); // receive_data[1]은 move.destination임
    						
    						state = GameData.PREPARE_MOVING; // state 값 바꿔줌.
    					} catch (SocketException socketException) {
    						break;
    					}
    					catch (IOException ioException) {
    						ioException.printStackTrace();
    					}
    				}
    			}
    		};
    		t.start();
    	}
    	
    	public void close() {
    		socket.close();
    	}
    	
    	class MyFrame extends JFrame implements ActionListener {
    		public MyFrame() {
    			super("NetworkGameClient");
    			//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    			
    			textField = new JTextField(30);
    			textField.addActionListener(this);
    			textArea = new JTextArea(10, 30);
    			textArea.setEditable(false);
    			button = new JButton();
    			button.addActionListener(this);
    			
    			add(button, BorderLayout.NORTH);
    			add(textField, BorderLayout.PAGE_END);
    			add(textArea, BorderLayout.CENTER);
    			pack();
    			setVisible(false);
    			
    			addWindowListener(new WindowAdapter(){ // 윈도우 닫는지 확인하는듯
    	            public void windowClosing(WindowEvent e){
    	                quit();
    	            }
    	        });
    		}
    		
    		 public void quit(){
    		        int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
    		                    "NetworkMessengerHost", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 버전 수정######
    		        if(option == JOptionPane.YES_OPTION)
    		            System.exit(0);
    		        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    		 }
    		
    		public void actionPerformed(ActionEvent evt) {
    			//String s = textField.getText();
    			//byte[] buffer = s.getBytes();
    			byte[] buffer = sendingM.getBytes();
    			DatagramPacket packet;
					
    			//패킷 생성
    			packet = new DatagramPacket(buffer, buffer.length, address, otherPort);
    			try {
    				socket.send(packet);
    				System.out.println("Send : " + sendingM); // 보내는거 확인
    			}
    			catch (IOException e) {
    				e.printStackTrace();
    			}
    			textArea.append( new String(buffer) );
    			textField.setText("");
    			textArea.setCaretPosition(textArea.getDocument().getLength());
    		}
    	}
    }
    
    public void playLocal(){
        Thread t = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case GameData.PLAYER1_MOVE:    
                            break;
                        case GameData.PLAYER2_MOVE:
                            if(gameEnded(GameData.PLAYER2)){
                                state = GameData.GAME_END;
                                break;
                            }                                
                            break;
                        case GameData.PREPARE_MOVING:
                            prepareAnimation();
                            break;
                        case GameData.MOVING:
                            animate();
                            break;                        
                        case GameData.GAME_END: return;
                    }
                    try{
                        Thread.sleep(3);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
    
    public void playNetwork(){
        Thread t_host = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case GameData.PLAYER1_MOVE:
                            break;
                        case GameData.PLAYER2_MOVE:
                        	if(gameEnded(GameData.PLAYER2)){
                                state = GameData.GAME_END;
                                break;
                            }      
                            break;
                        case GameData.PREPARE_MOVING:
                            prepareAnimation();
                            break;
                        case GameData.MOVING:
                            animate();
                            break;                        
                        case GameData.GAME_END: return;
                    }
                    try{
                        Thread.sleep(3);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        /*
        Thread t_client = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case GameData.PLAYER1_MOVE:
                            suspend();
                            break;
                        case GameData.PLAYER2_MOVE:
                            break;
                        case GameData.PREPARE_MOVING:
                            //prepareAnimation();
                            break;
                        case GameData.MOVING:
                        	resume();
                            //animate();
                            break;                        
                        case GameData.GAME_END: return;
                    }
                    try{
                        Thread.sleep(3);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };*/
        t_host.start();
        //t_client.start();
    }
    
    public boolean gameEnded(int player){
        int result = game.getResult(player);
        boolean end_game = false;
        String color ="";
        if(player == GameData.PLAYER2){
            color = (is_white)?"White":"Black";
        }else color = (is_white)?"Black":"White";
        if(result == GameData.CHECKMATE){
            showEndGameResult(color+" wins by CHECKMATE");
            end_game = true;
        }else if(result == GameData.DRAW){
            showEndGameResult("DRAW");
            end_game = true;
        }
        return end_game;
    }
    
    public void showEndGameResult(String message){
    	Thread t = new Thread(){
            public void run(){
            	history_saver.save( history_chesslocations );
            }
    	};
    	t.start();
    	
        int option;
        if( gametype != 2 ) { 
        	option = JOptionPane.showOptionDialog(null,
                message,"Game Over",0,JOptionPane.PLAIN_MESSAGE,
                null,new Object[]{"Play again","Cancel"},"Play again");
        }
        else {
        	option = JOptionPane.showOptionDialog(null,
                    message,"Game Over",0,JOptionPane.PLAIN_MESSAGE,
                    null,new Object[]{"OK"},"OK");
        }
        	
        if(option == 0){
        	if(gametype == 1) {
        		//gametype = 0; // gametype 다시 0으로 해줌
        		showPressLocalGameWarning();
        	}
        	else if(gametype == 2) {
        		gametype = 0; // gametype 다시 0으로 해줌
        		if( network_options.host_button.isSelected() == true) {
            		messengerHost.close();
            		networkgameHost.close();
            	}
            	else {
            		messengerClient.close();
            		networkgameClient.close();
            	}
        	}
        }
    }
    
    public void showPlayAgainAgain(){
        int option = JOptionPane.showOptionDialog(null,
        		"Do you REALLY want to play again??","Play again",0,JOptionPane.PLAIN_MESSAGE,
                null,new Object[]{"Play again","Cancel"},"Play again");
        if(option == 0){
        	if(gametype == 1) {
        		//gametype = 0; // gametype 다시 0으로 해줌
        		showPressLocalGameWarning();
        	}
        	else if(gametype == 2) {
        		gametype = 0; // gametype 다시 0으로 해줌        		
        		againGameNetwork();
        	}
        }
    }
    
    public void showPressLocalGameWarning(){
        JOptionPane.showMessageDialog(null,
                "Press the LocalGame button.\n",
                "Hey!",JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showNoHistoryWarning(){
        JOptionPane.showMessageDialog(null,
                "No history in that num.\n",
                "No History",JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showAlreadyPlayNetworkWarning(){
        JOptionPane.showMessageDialog(null,
                "You are already playing network game!\n",
                "Hey!",JOptionPane.WARNING_MESSAGE);
    }
    
    public void showUsingPortWarning(){
        JOptionPane.showMessageDialog(null,
                "That port is already using!\n",
                "Hey!",JOptionPane.WARNING_MESSAGE);
    }
    
    public void showWrongTurnWarning(){
        JOptionPane.showMessageDialog(null,
                "It's not your turn!\n",
                "Hey!",JOptionPane.WARNING_MESSAGE);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { // 얘는 어떤거 실행되고 있든 상관없이 실행할 수 있는 것들
        Object source = e.getSource();
        
        int select = 0;
        if(source == local_game)
    		select = GameData.BUTTON_LOCALGAME;
    	else if(source == network_game)
    		select = GameData.BUTTON_NETWORKGAME;
    	else if(source == history)
    		select = GameData.BUTTON_HISTORY;
    	else if(source == about)
    		select = GameData.BUTTON_ABOUT;
    	else if(source == quit)
    		select = GameData.BUTTON_QUIT;
    	else if(source == first)
    		select = GameData.BUTTON_FIRST;
    	else if(source == prev)
    		select = GameData.BUTTON_PREV;
    	else if(source == next)
    		select = GameData.BUTTON_NEXT;
    	else if(source == last)
    		select = GameData.BUTTON_LAST;
        
        switch(select) {
        case GameData.BUTTON_LOCALGAME :
        	localGame();
        	break;
        case GameData.BUTTON_NETWORKGAME :
        	if(network_options == null) {
        		network_options = new NetworkPane(this);
        	}
        	network_options.setVisible(true);
        	break;
        case GameData.BUTTON_HISTORY : // history이면 Historypane 띄어줌
        	if(history_button_pane == null ) {
            	history_button_pane = new HistoryPane(this);
            }
            history_button_pane.setVisible(true);
        	break;
        case GameData.BUTTON_ABOUT : // about이면 AboutPane 실행
        	AboutPane.createAndShowUI();
        	break;
        case GameData.BUTTON_QUIT : // 클릭된 소스가 quit() 실행.
        	quit();
        	break;
        case GameData.BUTTON_FIRST :
        	history_count = 0;
            history_pane.repaint();
        	break;
        case GameData.BUTTON_PREV :
        	if(history_count>0){
                history_count--;
                history_pane.repaint();
            }
        	break;
        case GameData.BUTTON_NEXT :
        	if(history_count<history_chesslocations.size()-1){
                history_count++;
                history_pane.repaint();
            }
        	break;
        case GameData.BUTTON_LAST :
        	history_count = history_chesslocations.size()-1;
            history_pane.repaint();
        	break;
        }
    }    

    @Override
    public void mouseEntered(MouseEvent e) { // 마우스 올렸을 때 아이콘 바꿔주기
        Object source = e.getSource();
        int select = 0;
        if(source == local_game)
    		select = GameData.BUTTON_LOCALGAME;
    	else if(source == network_game)
    		select = GameData.BUTTON_NETWORKGAME;
    	else if(source == history)
    		select = GameData.BUTTON_HISTORY;
    	else if(source == about)
    		select = GameData.BUTTON_ABOUT;
    	else if(source == quit)
    		select = GameData.BUTTON_QUIT;
    	else if(source == first)
    		select = GameData.BUTTON_FIRST;
    	else if(source == prev)
    		select = GameData.BUTTON_PREV;
    	else if(source == next)
    		select = GameData.BUTTON_NEXT;
    	else if(source == last)
    		select = GameData.BUTTON_LAST;
    	
        switch(select) {
        case GameData.BUTTON_LOCALGAME :
        	local_game.setIcon(icon_images.get(GameData.BUTTON_LOCALGAME_R));
        	break;
        case GameData.BUTTON_NETWORKGAME :
        	network_game.setIcon(icon_images.get(GameData.BUTTON_NETWORKGAME_R));
        	break;
        case GameData.BUTTON_HISTORY :
        	history.setIcon(icon_images.get(GameData.BUTTON_HISTORY_R));
        	break;
        case GameData.BUTTON_ABOUT :
        	about.setIcon(icon_images.get(GameData.BUTTON_ABOUT_R));
        	break;
        case GameData.BUTTON_QUIT :
        	quit.setIcon(icon_images.get(GameData.BUTTON_QUIT_R));
        	break;
        case GameData.BUTTON_FIRST :
        	first.setIcon(icon_images.get(GameData.BUTTON_FIRST_R));
        	break;
        case GameData.BUTTON_PREV :
        	prev.setIcon(icon_images.get(GameData.BUTTON_PREV_R));
        	break;
        case GameData.BUTTON_NEXT :
        	next.setIcon(icon_images.get(GameData.BUTTON_NEXT_R));
        	break;
        case GameData.BUTTON_LAST :
        	last.setIcon(icon_images.get(GameData.BUTTON_LAST_R));
        	break;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) { // 마우스 나가면 다시 아이콘 바꾸기
        Object source = e.getSource();
        int select = 0;
        if(source == local_game)
    		select = GameData.BUTTON_LOCALGAME;
    	else if(source == network_game)
    		select = GameData.BUTTON_NETWORKGAME;
    	else if(source == history)
    		select = GameData.BUTTON_HISTORY;
    	else if(source == about)
    		select = GameData.BUTTON_ABOUT;
    	else if(source == quit)
    		select = GameData.BUTTON_QUIT;
    	else if(source == first)
    		select = GameData.BUTTON_FIRST;
    	else if(source == prev)
    		select = GameData.BUTTON_PREV;
    	else if(source == next)
    		select = GameData.BUTTON_NEXT;
    	else if(source == last)
    		select = GameData.BUTTON_LAST;
        
        switch(select) {
        case GameData.BUTTON_LOCALGAME :
        	local_game.setIcon(icon_images.get(GameData.BUTTON_LOCALGAME));
        	break;
        case GameData.BUTTON_NETWORKGAME :
        	network_game.setIcon(icon_images.get(GameData.BUTTON_NETWORKGAME));
        	break;
        case GameData.BUTTON_HISTORY :
        	history.setIcon(icon_images.get(GameData.BUTTON_HISTORY));
        	break;
        case GameData.BUTTON_ABOUT :
        	about.setIcon(icon_images.get(GameData.BUTTON_ABOUT));
        	break;
        case GameData.BUTTON_QUIT :
        	quit.setIcon(icon_images.get(GameData.BUTTON_QUIT));
        	break;
        case GameData.BUTTON_FIRST :
        	first.setIcon(icon_images.get(GameData.BUTTON_FIRST));
        	break;
        case GameData.BUTTON_PREV :
        	prev.setIcon(icon_images.get(GameData.BUTTON_PREV));
        	break;
        case GameData.BUTTON_NEXT :
        	next.setIcon(icon_images.get(GameData.BUTTON_NEXT));
        	break;
        case GameData.BUTTON_LAST :
        	last.setIcon(icon_images.get(GameData.BUTTON_LAST));
        	break;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {}
    
    // 전체프레임의 Board Pane임
    public class ChessBoardPane extends JPanel implements MouseListener{
        Image animating_image;
        int movingX,movingY,desX,desY,deltaX,deltaY;

        public ChessBoardPane(){
        	
            setPreferredSize(new Dimension(450,495)); // 전체프레임 크기.
            setBackground(bg_color); // 배경색 지정
            addMouseListener(this); // 마우스에 만응하게!
        }
        @Override
        public void paintComponent(Graphics g){ // 얘는 실행 중에 플레이어에 반응해서 바뀌는 애일듯!
            if(chesslocation.chessboard == null) return;
            super.paintComponent(g);  // 부모의 paintComponent 부르는 듯!
            g.drawImage(images.get(GameData.TITLE_CHESSPROJECT),20,36,this); // chessproject 글자 넣을 위치 결정
            g.drawImage(images.get(GameData.CHESSBOARD_MAIN),20,65,this); // 보드 이미지 넣을 위치
            for (int i = 0; i < chesslocation.chessboard.length; i++) {
                if (chesslocation.chessboard[i] == GameData.IMPOSSIBLE) continue;                                                                
                int x = i%10; // x좌표 얻기
                int y = (i-x)/10; // y좌표 얻기
                
                if (piece_selected && i == move.source_location) { // 피스가 눌러져있고, 피스 위치랑 i랑 같다면 피스 위치에 클릭됬다는 이미지 넣어줌
                    g.drawImage(images.get(GameData.CLICK_TILE), x * 45, y * 45,this); // glow 이미지 픽셀이 45x45임            
                }else if(!piece_selected && move.destination == i && // 움직이고자 클릭했을 때 움직이는 위치에 glow2 띄어줌.  사실 (chesslocation.chessboard[i]==GameData.NOTHING || chesslocation.chessboard[i]<0) 이부분은 빼도 됨
                        (chesslocation.chessboard[i]==GameData.NOTHING || chesslocation.chessboard[i]<0)){
                    g.drawImage(images.get(GameData.MOVE_TILE), x * 45, y * 45, this); // 움직일 수 있는 위치라는 이미지 보여줌.                                   
                }
                
                if (chesslocation.chessboard[i] == GameData.NOTHING) continue;
                
                if(state == GameData.MOVING && i==move.source_location) continue; // 애니메이션 상태일 경우, 움직이는 말의 원래 위치에는 말 그리지 않음.
                if (chesslocation.chessboard[i] > 0) { // 플레이어 말들 그려줌
                    int piece = chesslocation.player1_pieces[chesslocation.chessboard[i]].id;
                    g.drawImage(images.get(piece),x*45,y*45,this);
                }else{ // 컴퓨터 말들 그려줌
                    int piece = chesslocation.player2_pieces[-chesslocation.chessboard[i]].id;
                    g.drawImage(images.get(-piece),x*45,y*45,this);
                }               
            }  
            if(state == GameData.MOVING){ // super.paintComponent(g)를 부르면 멤버변수들 업데이트 되는 듯?
                g.drawImage(animating_image,movingX,movingY,this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        	if( gametype == 1 ) { // 플레이어 vs 플레이어.
        		int location = boardValue(e.getY())*10+boardValue(e.getX()); // boardValue 함수를 통해 해당 칸의 위치 알아냄.       
                if(chesslocation.chessboard[location] == GameData.IMPOSSIBLE) return; // 불가능한 칸 클릭시 return
                if(state == GameData.PLAYER1_MOVE) {
                	if((!piece_selected || chesslocation.chessboard[location]>0) && chesslocation.chessboard[location] != GameData.NOTHING){ // 피스 선택 안되어 있었고, 플레이어1의 말일 경우
                		if(chesslocation.chessboard[location]>0){
                			piece_selected = true; // 피스 선택됬다고 수정
                			move.source_location = location; // move의 source 위치 지정
                		}
                	}else if(piece_selected && validMove(location)){ // 피스 선택되어있었고, 움직일 수 있는 자리면,
                		piece_selected = false; // 피스 선택되었던거 이제 선택 안됬다고 수정
                		move.destination = location; // 도착 위치 지정
                		state = GameData.PREPARE_MOVING; // 에니메이션 해야되니 이걸로 값 바꿔주는 듯
                	}
                	repaint(); // 이게 불리면 paintComponent 불리는듯?
                }
                else if(state == GameData.PLAYER2_MOVE) {
                	if((!piece_selected || chesslocation.chessboard[location]<0) && chesslocation.chessboard[location] != GameData.NOTHING){ // 피스 선택 안되어 있었고, 플레이어2의 말일 경우
                		if(chesslocation.chessboard[location]<0){
                			piece_selected = true; // 피스 선택됬다고 수정
                			move.source_location = location; // move의 source 위치 지정
                		}
                	}else if(piece_selected && validMove(location)){ // 피스 선택되어있었고, 움직일 수 있는 자리면,
                		piece_selected = false; // 피스 선택되었던거 이제 선택 안됬다고 수정
                		move.destination = location; // 도착 위치 지정
                		state = GameData.PREPARE_MOVING; // 에니메이션 해야되니 이걸로 값 바꿔주는 듯
                	}
                	repaint(); // 이게 불리면 paintComponent 불리는듯?
                }
        	}
        	else if( gametype == 2 ) { // 플레이어 vs 플레이어.
        		int location = boardValue(e.getY())*10+boardValue(e.getX()); // boardValue 함수를 통해 해당 칸의 위치 알아냄.       
                if(chesslocation.chessboard[location] == GameData.IMPOSSIBLE) return; // 불가능한 칸 클릭시 return
                
                if(gametype == 2 && state == GameData.PLAYER1_MOVE && network_options.host_button.isSelected() == false) {
                	showWrongTurnWarning();
                }
                else if(state == GameData.PLAYER1_MOVE) {
                	if((!piece_selected || chesslocation.chessboard[location]>0) && chesslocation.chessboard[location] != GameData.NOTHING){ // 피스 선택 안되어 있었고, 플레이어1의 말일 경우
                		if(chesslocation.chessboard[location]>0){
                			piece_selected = true; // 피스 선택됬다고 수정
                			move.source_location = location; // move의 source 위치 지정
                		}
                	}else if(piece_selected && validMove(location)){ // 피스 선택되어있었고, 움직일 수 있는 자리면,
                		piece_selected = false; // 피스 선택되었던거 이제 선택 안됬다고 수정
                		move.destination = location; // 도착 위치 지정
                		
                		if(gametype == 2 && ( network_options.host_button.isSelected() == true && state == GameData.PLAYER1_MOVE)) { // 네트워크게임에서 값 보내주기 위해서.
                			send = true;
                			sendingM = new String( Integer.toString(move.source_location) + " " + Integer.toString(move.destination) );
                			networkgameHost.button.doClick();
                		}
                		
                		state = GameData.PREPARE_MOVING; // 에니메이션 해야되니 이걸로 값 바꿔주는 듯
                	}
                	repaint(); // 이게 불리면 paintComponent 불리는듯?
                }
                else if( gametype == 2 && state == GameData.PLAYER2_MOVE && network_options.host_button.isSelected() == true) {
                	showWrongTurnWarning();
                }
                else if(state == GameData.PLAYER2_MOVE) {
                	if((!piece_selected || chesslocation.chessboard[location]<0) && chesslocation.chessboard[location] != GameData.NOTHING){ // 피스 선택 안되어 있었고, 플레이어2의 말일 경우
                		if(chesslocation.chessboard[location]<0){
                			piece_selected = true; // 피스 선택됬다고 수정
                			move.source_location = location; // move의 source 위치 지정
                		}
                	}else if(piece_selected && validMove(location)){ // 피스 선택되어있었고, 움직일 수 있는 자리면,
                		piece_selected = false; // 피스 선택되었던거 이제 선택 안됬다고 수정
                		move.destination = location; // 도착 위치 지정
                		
                		if(gametype == 2 && (network_options.host_button.isSelected() == false && state == GameData.PLAYER2_MOVE)) { // 네트워크 게임에서 값 보내주기 위해서.
                			send = true;
                			sendingM = new String( Integer.toString(move.source_location) + " " + Integer.toString(move.destination) );
                			networkgameClient.button.doClick();
                		}
                		
                		state = GameData.PREPARE_MOVING; // 에니메이션 해야되니 이걸로 값 바꿔주는 듯
                	}
                	repaint(); // 이게 불리면 paintComponent 불리는듯?
                }
        	}
        	
        }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }
    
    //히스토리 페인
    public class HistoryBoardPane extends JPanel{
        public HistoryBoardPane(){
            setBackground(bg_color);
            setPreferredSize(new Dimension(300,330));            
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(images.get(GameData.TITLE_HISTORY),20,15,this); // 이름 이미지 넣기
            g.drawImage(images.get(GameData.CHESSBOARD_HISTORY),14,44,this); // 히스토리보드 이미지 넣기
            if(history_chesslocations == null) return;
            if(history_chesslocations.size()<=0) return;
            ChessLocation _chesslocation = history_chesslocations.get(history_count);
            for(int i=0; i<_chesslocation.chessboard.length -11; i++){
                if(_chesslocation.chessboard[i] == GameData.NOTHING) continue; // 빈칸이면 이미저 넣을 이유 없음
                if(_chesslocation.chessboard[i] == GameData.IMPOSSIBLE) continue; // 마찬가지로 이미지 넣을 이유 없음
                int x = i%10; // x 위치 저장
                int y = (i-x)/10; // y 위치 저장
                if (_chesslocation.chessboard[i] > 0) {          
                    int piece = _chesslocation.player1_pieces[_chesslocation.chessboard[i]].id;
                    g.drawImage(images.get(piece+10),x*30,y*30,this); // 조그만한 보드의 한 픽셀 크기는 30x30
                }else{
                    int piece = _chesslocation.player2_pieces[-_chesslocation.chessboard[i]].id;
                    g.drawImage(images.get(-piece+10),x*30,y*30,this);
                }
            }
        }
    }
    
    public boolean validMove(int destination) { // computer가 아닌 player의 validMove 체크!
        int source = move.source_location;
        int destination_square = chesslocation.chessboard[destination];
        if(destination_square == GameData.IMPOSSIBLE) return false;
        
        // 안전한 움직임 체크
     	if( state == GameData.PLAYER1_MOVE)
           	if(!game.safeMove(GameData.PLAYER1,source,destination)) return false;
       	if( state == GameData.PLAYER2_MOVE)
           	if(!game.safeMove(GameData.PLAYER2,source,destination)) return false;
        // 안전한 움직임이라면, 가능한 위치인지 확인
        boolean valid = false;
        if( state == GameData.PLAYER1_MOVE) {
        	int piece_value = chesslocation.player1_pieces[chesslocation.chessboard[source]].id;                        
            switch(piece_value){
                case GameData.PAWN:
                    if(destination == source-10 && destination_square == GameData.NOTHING) valid = true;
                    if(destination == source-20 && chesslocation.chessboard[source-10] == GameData.NOTHING &&
                            destination_square == GameData.NOTHING && source>80) valid = true;
                    if(destination == source-9 && destination_square<0) valid = true;
                    if(destination == source-11 && destination_square<0) valid = true;
                    break;
                case GameData.KNIGHT:
                case GameData.KING:
                    if(piece_value == GameData.KING) valid = checkCastling(destination);
                    int[] destinations = null;
                    if(piece_value == GameData.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                        source-12,source+12,source-8,source+8};
                    else destinations = new int[]{source+1,source-1,source+10,source-10,
                        source+11,source-11,source+9,source-9};
                    for(int i=0; i<destinations.length; i++){
                        if(destinations[i] == destination){
                            if(destination_square == GameData.NOTHING || destination_square<0){
                                valid = true;
                                break;
                            }
                        }
                    }                
                    break;
                case GameData.BISHOP:
                case GameData.ROOK:
                case GameData.QUEEN:
                    int[] deltas = null;
                    if(piece_value == GameData.BISHOP) deltas = new int[]{11,-11,9,-9};
                    if(piece_value == GameData.ROOK) deltas = new int[]{1,-1,10,-10};
                    if(piece_value == GameData.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                    for (int i = 0; i < deltas.length; i++) {
                        int des = source + deltas[i]; 
                        valid = true;
                        while (destination != des) { 
                            destination_square = chesslocation.chessboard[des];  
                            if(destination_square != GameData.NOTHING){
                                valid = false;
                                break;
                            }                        
                            des += deltas[i];
                        }
                        if(valid) break;
                    }
                    break;
            }
        }
        else if( state == GameData.PLAYER2_MOVE) {
        	int piece_value = chesslocation.player2_pieces[-chesslocation.chessboard[source]].id;                        
            switch(piece_value){
                case GameData.PAWN:
                    if(destination == source+10 && destination_square == GameData.NOTHING) valid = true;
                    if(destination == source+20 && chesslocation.chessboard[source+10] == GameData.NOTHING &&
                            destination_square == GameData.NOTHING && source<40) valid = true;
                    if(destination == source+9 && (destination_square>0 && destination_square != GameData.NOTHING && destination_square != GameData.IMPOSSIBLE )) valid = true;
                    if(destination == source+11 && (destination_square>0 && destination_square != GameData.NOTHING && destination_square != GameData.IMPOSSIBLE )) valid = true;
                    break;
                case GameData.KNIGHT:
                case GameData.KING:
                    if(piece_value == GameData.KING) valid = checkCastling(destination);
                    int[] destinations = null;
                    if(piece_value == GameData.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,
                        source-12,source+12,source-8,source+8};
                    else destinations = new int[]{source+1,source-1,source+10,source-10,
                        source+11,source-11,source+9,source-9};
                    for(int i=0; i<destinations.length; i++){
                        if(destinations[i] == destination){
                            if(destination_square == GameData.NOTHING || (destination_square>0 && destination_square != GameData.NOTHING && destination_square != GameData.IMPOSSIBLE )){
                                valid = true;
                                break;
                            }
                        }
                    }                
                    break;
                case GameData.BISHOP:
                case GameData.ROOK:
                case GameData.QUEEN:
                    int[] deltas = null;
                    if(piece_value == GameData.BISHOP) deltas = new int[]{11,-11,9,-9};
                    if(piece_value == GameData.ROOK) deltas = new int[]{1,-1,10,-10};
                    if(piece_value == GameData.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                    for (int i = 0; i < deltas.length; i++) {
                        int des = source + deltas[i]; 
                        valid = true;
                        while (destination != des) {
                            destination_square = chesslocation.chessboard[des];  
                            if(destination_square != GameData.NOTHING){
                                valid = false;
                                break;
                            }                        
                            des += deltas[i];
                        }
                        if(valid) break;
                    }
                    break;
            }
        }
                
        return valid;
    }
    
    public boolean checkCastling(int destination){        
        Piece king = chesslocation.player1_pieces[8];
        Piece right_rook = chesslocation.player1_pieces[6];
        Piece left_rook = chesslocation.player1_pieces[5];
        
        if(king.has_moved) return false;              
        int source = move.source_location;
        
        if(right_rook == null && left_rook == null) return false;
        if(right_rook != null && right_rook.has_moved && 
                left_rook != null && left_rook.has_moved) return false;
        if(is_white){
            if(source != 95) return false;            
            if(destination != 97 && destination != 93) return false;
            if(destination == 97){
                if(chesslocation.chessboard[96] != GameData.NOTHING) return false;
                if(chesslocation.chessboard[97] != GameData.NOTHING) return false;
                if(!game.safeMove(GameData.PLAYER1,source,96)) return false;
                if(!game.safeMove(GameData.PLAYER1,source,97)) return false;
            }else if(destination == 93){
                if(chesslocation.chessboard[94] != GameData.NOTHING) return false;
                if(chesslocation.chessboard[93] != GameData.NOTHING) return false;
                if(!game.safeMove(GameData.PLAYER1,source,94)) return false;
                if(!game.safeMove(GameData.PLAYER1,source,93)) return false;
            }
        }else{
            if(source != 94) return false;            
            if(destination != 92 && destination != 96) return false;
            if(destination == 92){
                if(chesslocation.chessboard[93] != GameData.NOTHING) return false;
                if(chesslocation.chessboard[92] != GameData.NOTHING) return false;
                if(!game.safeMove(GameData.PLAYER1,source,93)) return false;
                if(!game.safeMove(GameData.PLAYER1,source,92)) return false;
            }else if(destination == 96){
                if(chesslocation.chessboard[95] != GameData.NOTHING) return false;
                if(chesslocation.chessboard[96] != GameData.NOTHING) return false;
                if(!game.safeMove(GameData.PLAYER1,source,95)) return false;
                if(!game.safeMove(GameData.PLAYER1,source,96)) return false;
            }
        }        
        return castling=true;
    }
    
    public int boardValue(int value){ // 보드 칸의 픽셀 크기로 나눠줌.
        return value/45;
    }
    
    public void prepareAnimation(){
        int animating_image_key = 0;
        if(chesslocation.chessboard[move.source_location]>0){
            animating_image_key = chesslocation.player1_pieces[chesslocation.chessboard[move.source_location]].id;
        }else {
            animating_image_key = -chesslocation.player2_pieces[-chesslocation.chessboard[move.source_location]].id;
        }
        board_pane.animating_image = images.get(animating_image_key);
        int x = move.source_location%10;        
        int y = (move.source_location-x)/10;
        board_pane.desX = move.destination%10;
        board_pane.desY = (move.destination-board_pane.desX)/10;
        int dX = board_pane.desX-x;
        int dY = board_pane.desY-y;           
        board_pane.movingX = x*45;
        board_pane.movingY = y*45;
        if(Math.abs(dX)>Math.abs(dY)){
            if(dY == 0){
            	if( dX>0 )
            		board_pane.deltaX = 1;
            	else
            		board_pane.deltaX = -1;
                board_pane.deltaY = 0;
            }else{
            	if( dX>0 ) board_pane.deltaX = Math.abs(dX/dY);
            	else       board_pane.deltaX = -(Math.abs(dX/dY));
            	
                if ( dY>0 ) board_pane.deltaY = 1;
                else     	board_pane.deltaY = -1;
            }
        }else{
            if(dX == 0){
            	if( dY > 0 ) board_pane.deltaY = 1;
            	else 		 board_pane.deltaY = -1;
            	
                board_pane.deltaX = 0;
            }else{
            	if( dX>0 ) board_pane.deltaX = 1;
            	else 	   board_pane.deltaX = -1;
            	
            	if( dY>0 ) board_pane.deltaY = Math.abs(dY/dX);
            	else 	   board_pane.deltaY = -(Math.abs(dY/dX));
            }
        }          
        state = GameData.MOVING;
    }
    
    public void animate(){
        if (board_pane.movingX == board_pane.desX * 45 && board_pane.movingY == board_pane.desY * 45) {                                           
            board_pane.repaint();            
            int source_square = chesslocation.chessboard[move.source_location];            
            if(source_square>0){                
                state = GameData.PLAYER2_MOVE;                                               
            }else {
                if(move.destination > 90 && move.destination<98 
                        && chesslocation.player2_pieces[-source_square].id == GameData.PAWN)
                    promotePlayer2Pawn();
                state = GameData.PLAYER1_MOVE;
            }                        
            chesslocation.update(move);       
            if(source_square>0){
                if(castling){   
                    prepareCastlingAnimation();
                      state = GameData.PREPARE_MOVING;
                }else if(move.destination > 20 && move.destination < 29 && 
                        chesslocation.player1_pieces[source_square].id == GameData.PAWN){
                    promotePlayer1Pawn();                    
                }
            }else{
                if (gameEnded(GameData.PLAYER1)) {
                    state = GameData.GAME_END;
                    return;
                }
            }
            if(!castling && state != GameData.PROMOTION) 
                newHistoryChessLocation();
            if(castling) castling = false;          
        }
        board_pane.movingX += board_pane.deltaX;
        board_pane.movingY += board_pane.deltaY;
        board_pane.repaint();
    }
    
    public void promotePlayer1Pawn(){        
        promotion_pane.location = move.destination;
        promotion_pane.index = chesslocation.chessboard[move.destination];
        promotion_pane.setVisible(true);
        state = GameData.PROMOTION;
    }
    public void promotePlayer2Pawn(){
        int piece_index = chesslocation.chessboard[move.source_location];
        chesslocation.player2_pieces[-piece_index] = new Piece(GameData.QUEEN,move.destination);
    }
    public void prepareCastlingAnimation(){
        if(move.destination == 97 || move.destination == 96){
            move.source_location = 98;
            move.destination -= 1;
        }else if(move.destination == 92 || move.destination == 93){
            move.source_location = 91;
            move.destination += 1;
        }
    }
    public void newHistoryChessLocation(){        
        history_chesslocations.add(new ChessLocation(chesslocation));
        history_count = history_chesslocations.size()-1;
        history_pane.repaint();
    }
    public void loadPieceImages(){
        String[] resource_keys = {"pawn","knight","bishop","rook","queen","king"};
        int[] images_keys = {GameData.PAWN,GameData.KNIGHT,GameData.BISHOP,GameData.ROOK,GameData.QUEEN,GameData.KING};
        try{
            for(int i=0; i<resource_keys.length; i++){             
                images.put(images_keys[i],ImageIO.read(resource.getResource((is_white?"white_":"black_")+resource_keys[i])));
                images.put(-images_keys[i],ImageIO.read(resource.getResource((is_white?"black_":"white_")+resource_keys[i])));   
                images.put(images_keys[i]+10,ImageIO.read(resource.getResource((is_white?"white_":"black_")+resource_keys[i]+"_mini")));
                images.put(-images_keys[i]+10,ImageIO.read(resource.getResource((is_white?"black_":"white_")+resource_keys[i]+"_mini"))); 
            }               
        }catch(IOException ex){
            ex.printStackTrace();
        }        
    }
    public void loadBoardImages(){
        try{ 
            images.put(GameData.CHESSBOARD_MAIN,ImageIO.read(resource.getResource("chessboard_main"))); // Map<Integer,Image> images 에 int와 image 저장 
            images.put(GameData.CHESSBOARD_HISTORY,ImageIO.read(resource.getResource("chessboard_history")));
            images.put(GameData.CLICK_TILE,ImageIO.read(resource.getResource("click_tile"))); // 클릭했을 때
            images.put(GameData.MOVE_TILE,ImageIO.read(resource.getResource("move_tile"))); // 움직일 수 있는 칸   
            images.put(GameData.TITLE_HISTORY,ImageIO.read(resource.getResource("title_history")));
            images.put(GameData.TITLE_CHESSPROJECT,ImageIO.read(resource.getResource("title_chessproject")));
        }catch(IOException ex){
            ex.printStackTrace();
        }        
    }
    public void loadMenuIcons(){
        icon_images.put(GameData.BUTTON_LOCALGAME,new ImageIcon(resource.getResource("button_localgame")));
        icon_images.put(GameData.BUTTON_LOCALGAME_R,new ImageIcon(resource.getResource("button_localgame_r")));
        icon_images.put(GameData.BUTTON_NETWORKGAME,new ImageIcon(resource.getResource("button_networkgame")));
        icon_images.put(GameData.BUTTON_NETWORKGAME_R,new ImageIcon(resource.getResource("button_networkgame_r")));
        icon_images.put(GameData.BUTTON_HISTORY,new ImageIcon(resource.getResource("button_history")));
        icon_images.put(GameData.BUTTON_HISTORY_R,new ImageIcon(resource.getResource("button_history_r")));
        icon_images.put(GameData.BUTTON_ABOUT,new ImageIcon(resource.getResource("button_about")));
        icon_images.put(GameData.BUTTON_ABOUT_R,new ImageIcon(resource.getResource("button_about_r")));
        icon_images.put(GameData.BUTTON_QUIT,new ImageIcon(resource.getResource("button_quit")));
        icon_images.put(GameData.BUTTON_QUIT_R,new ImageIcon(resource.getResource("button_quit_r")));
        
        icon_images.put(GameData.BUTTON_FIRST,new ImageIcon(resource.getResource("button_first")));
        icon_images.put(GameData.BUTTON_FIRST_R,new ImageIcon(resource.getResource("button_first_r")));
        icon_images.put(GameData.BUTTON_NEXT,new ImageIcon(resource.getResource("button_next")));
        icon_images.put(GameData.BUTTON_NEXT_R,new ImageIcon(resource.getResource("button_next_r")));
        icon_images.put(GameData.BUTTON_PREV,new ImageIcon(resource.getResource("button_previous")));
        icon_images.put(GameData.BUTTON_PREV_R,new ImageIcon(resource.getResource("button_previous_r")));
        icon_images.put(GameData.BUTTON_LAST,new ImageIcon(resource.getResource("button_last")));
        icon_images.put(GameData.BUTTON_LAST_R,new ImageIcon(resource.getResource("button_last_r")));
    }
    
    public void quit(){
        int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
                    "ChessProject", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 버전 수정######
        if(option == JOptionPane.YES_OPTION)
            System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    public void localGame(){ // localGame frame에 관한 것
        int option = JOptionPane.showConfirmDialog(null,"Do you want to play local game?", 
                    "StartLocalGame", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 버전 수정######
        if(option == JOptionPane.YES_OPTION) {
        	newGameLocal();
        }
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    /*
    public void networkGame() {
        int option = JOptionPane.showConfirmDialog(null,"Do you want to play network game?", 
        			"networkGame", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 버전 수정######
        if(option == JOptionPane.YES_OPTION)
        	System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    */
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                try{
                    boolean nimbusFound = false;
                        for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
                            if(info.getName().equals("Nimbus")){
                                UIManager.setLookAndFeel(info.getClassName());
                                nimbusFound = true;
                                break;
                            }
                        }
                        if(!nimbusFound){
                            int option = JOptionPane.showConfirmDialog(null,
                                    "Nimbus Look And Feel not found\n"+
                                    "Do you want to proceed?",
                                    "Warning",JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                            if(option == JOptionPane.NO_OPTION){
                                System.exit(0);
                            }
                        }
                    ChessProject cp = new ChessProject();
                   
                    cp.setLocationRelativeTo(null); // 화면 가운데 뜨게.
                    cp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //x 버튼 누르면 닫히게.
                    cp.setResizable(false); //창 크기 변경 x
                    cp.setVisible(true); 
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, e.getStackTrace());
                    e.printStackTrace();
                }
            }
        });
    }
}
