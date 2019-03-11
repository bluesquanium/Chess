package chessproject;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NetworkMessengerClient {
	public JTextField textField;
	public JTextArea textArea;
	DatagramSocket socket;
	DatagramPacket packet;
	InetAddress address = null;
	final int myPort = GameData.MESSENGER_CLIENT_PORT;
	final int otherPort = GameData.MESSENGER_HOST_PORT;
	
	public NetworkMessengerClient(String _opponent_address) throws IOException {
		address = InetAddress.getByName( _opponent_address );
		//System.out.println(_opponent_address); // 주소 잘 받았나 확인
		socket = new DatagramSocket(myPort);
	}
	
	public void process() {
		MyFrame f = new MyFrame();
		Thread t = new Thread(){
			public void run() {
				while (true) {
					try
					{
						byte[] buf = new byte[256];
						packet = new DatagramPacket(buf, buf.length);
						socket.receive(packet);;
						textArea.append("상대방 : " + new String(buf) + "\n");
					} catch (SocketException socketException) {
						f.setVisible(false);
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
			super("MessengerClient");
			//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			textField = new JTextField(30);
			textField.addActionListener(this);
			
			textArea = new JTextArea(25, 30);
			textArea.setEditable(false);
			
			add(textField, BorderLayout.PAGE_END);
			add(textArea, BorderLayout.CENTER);
			pack();
			setVisible(true);
			setLocationRelativeTo(null);
			
			addWindowListener(new WindowAdapter(){ // 윈도우 닫는지 확인하는듯
	            public void windowClosing(WindowEvent e){
	                quit();
	            }
	        });
		}
		
		public void quit(){
	        int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
	                    "NetworkMessengerClient", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 버전 수정######
	        if(option == JOptionPane.YES_OPTION)
	            System.exit(0);
	        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	        }
		
		public void actionPerformed(ActionEvent evt) {
			String s = textField.getText();
			byte[] buffer = s.getBytes();
			DatagramPacket packet;
			
			//패킷 생성
			packet = new DatagramPacket(buffer, buffer.length, address, otherPort);
			try {
				socket.send(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append("나 : " + s + "\n");
			textField.selectAll();
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}
	/*
	public static void main(String[] args) throws IOException {
		MessengerB m = new MessengerB();
		m.process();
	}
	*/
}
