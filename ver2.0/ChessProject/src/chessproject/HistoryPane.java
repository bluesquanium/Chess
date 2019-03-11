package chessproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HistoryPane extends JFrame implements ActionListener{
    JSlider levelSlider;
    JButton ok;
    JButton cancel;
    ChessProject chessmate;

   public HistoryPane(ChessProject chessmate){
       super("Histories");
       this.chessmate = chessmate;
       JPanel mainPane = new JPanel(new BorderLayout());
       mainPane.add(createLevelPane(),BorderLayout.NORTH);
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
   public JPanel createLevelPane(){
       levelSlider = new JSlider(JSlider.HORIZONTAL,1,GameData.NUM_OF_HISTORY,GameData.NUM_OF_HISTORY/2); // ## 기보 저장 수 변경 가능
       JPanel levelPane = new JPanel();        
       levelSlider.setMajorTickSpacing(1);
       levelSlider.setPaintTicks(true);
       levelSlider.setPaintLabels(true);
       levelPane.add(levelSlider);
       levelPane.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createEmptyBorder(5,5,5,5),
               BorderFactory.createTitledBorder("Select History Number.")));
       return levelPane;
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
   public void actionPerformed(ActionEvent e){
       if(e.getSource() == ok){
           chessmate.state = GameData.GAME_END;
           chessmate.newHistoryGame();
           
       }
       setVisible(false); // 한번 실행되면 꺼지지는 않음
   }
}
