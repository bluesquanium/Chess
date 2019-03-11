package chessproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NetworkPane extends JFrame implements ActionListener{
	public JTextField textField;
    JRadioButton host_button;
    JRadioButton client_button;
    JButton ok;
    JButton cancel;
    final static int WHITE = 0;
    final static int BLACK = 1;
    ChessProject chessmate;

   public NetworkPane(ChessProject chessmate){
       super("Network Game");
       this.chessmate = chessmate;
       JPanel mainPane = new JPanel(new BorderLayout());
       mainPane.add(createColorPane(),BorderLayout.NORTH);
       mainPane.add(createTextFieldPane(),BorderLayout.CENTER);
       mainPane.add(createButtonPane(),BorderLayout.SOUTH);
       mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
       setContentPane(mainPane);
       pack();
       setLocationRelativeTo(null);
       setResizable(false);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       ok.addActionListener(this);
       cancel.addActionListener(this);
   }

   public JPanel createColorPane(){
       JPanel colorPane = new JPanel(new GridLayout(1,2));
       host_button = new JRadioButton("Host",true); // white
       client_button = new JRadioButton("Client");  // black
       ButtonGroup group = new ButtonGroup();
       group.add(host_button);
       group.add(client_button);
       colorPane.add(host_button);
       colorPane.add(client_button);
       colorPane.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createEmptyBorder(5,5,5,5),
               BorderFactory.createTitledBorder("Host or Client?")));
       return colorPane;
   }
   public JPanel createButtonPane(){
       JPanel buttonPane = new JPanel(new BorderLayout());
       JPanel pane = new JPanel(new GridLayout(1,2,5,0));
       pane.add(ok = new JButton("OK"));
       pane.add(cancel = new JButton("Cancel"));
       buttonPane.add(pane,BorderLayout.EAST);
       buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
       return buttonPane;
   }
   
   public JPanel createTextFieldPane(){
       JPanel textFieldPane = new JPanel();
       JPanel pane = new JPanel();
       pane.add( textField = new JTextField(15) );
       textFieldPane.add(pane,BorderLayout.EAST);
       textFieldPane.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createEmptyBorder(5,5,5,5),
               BorderFactory.createTitledBorder("Opponent's IP address?")));
       return textFieldPane;
   }
   
   public void actionPerformed(ActionEvent e){
       if(e.getSource() == ok){
           setVisible(false);
           chessmate.newGameNetwork();           
       }
       setVisible(false); // 한번 실행되면 꺼지지는 않음
   }
}
