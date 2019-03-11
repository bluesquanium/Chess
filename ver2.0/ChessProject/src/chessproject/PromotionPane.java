/* check(0.5)
 * 
 */

package chessproject;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class PromotionPane extends JDialog implements ActionListener{
    int index;
    int location;
    JPanel main_pane;
    ChessProject chessmate;

    public PromotionPane(ChessProject chessmate){
        setTitle("New Piece");
        this.chessmate = chessmate;
        main_pane = new JPanel(new GridLayout(1,4,10,0));
        Resource resource = new Resource();

        int[] cmdActions = {
            GameData.QUEEN,GameData.ROOK,GameData.BISHOP,GameData.KNIGHT
        };        
        for(int i=0; i<cmdActions.length; i++){
            JButton button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(cmdActions[i]+"");
            main_pane.add(button);
        }
        setContentPane(main_pane);        
        setResizable(false);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                resumeGame(GameData.QUEEN);
            }
        });
    }
    public void setIcons(boolean white){
        Component[] components = main_pane.getComponents();
        Resource resource = new Resource();
        String[] resourceStrings = {"queen","rook","bishop","knight"};
        for(int i=0; i<components.length; i++){
            JButton button = (JButton)components[i];
            button.setIcon(new ImageIcon(
                    resource.getResource((white?"white_":"black_")+resourceStrings[i])));
        }
        pack();
        setLocationRelativeTo(null);
    }
    public void actionPerformed(ActionEvent e){
        int promotion_piece = Integer.parseInt(e.getActionCommand());
        setVisible(false);
        resumeGame(promotion_piece);
    }
    public void resumeGame(int promotion_piece){  
        chessmate.chesslocation.player1_pieces[index] = new Piece(promotion_piece,location);
        chessmate.newHistoryChessLocation();
        chessmate.board_pane.repaint();
        chessmate.state = GameData.PLAYER2_MOVE;
    }
}
