/* check(1)
 *
 */

package chessproject;

import javax.swing.*;
import java.awt.*;


public class AboutPane extends JPanel{
    public AboutPane(){
        setLayout(new BorderLayout());
        JPanel northPane = new NorthPane();       
        JPanel centerPane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();        
        c.insets = new Insets(4,4,4,4); // 무슨용도?
        c.fill = GridBagConstraints.HORIZONTAL; // 큰 차이 모르겠다.

        String[][] values = new String[][]{
            {"Project","ChessProject "+GameData.VERSION},
            {"Category", GameData.CATEGORY},
            {"Author", GameData.AUTHOR},
            {"Recently Updated ", GameData.UPDATED_DATE}
        };
        for(int i=0; i<values.length; i++){
            JLabel header = new JLabel(values[i][0]+": ");
            header.setFont(new Font(header.getFont().getName(),Font.BOLD,13));
            JLabel data = new JLabel(values[i][1]);
            c.gridx = 0;
            c.gridy = i; // default로 해도 되는데 정확히 컨트롤하기 위해서인 듯!
            centerPane.add(header,c);
            c.gridx = 1;
            centerPane.add(data,c);
        }
        centerPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // 상하좌우 여백 설정

        add(northPane,BorderLayout.NORTH); // 실질적으로 패널을 추가
        add(centerPane,BorderLayout.CENTER); // 실질적으로 패널을 추가
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // 전체 틀의 사하좌우 여백 설정
    }    
    public static void createAndShowUI(){                   
        JFrame f = new JFrame("AboutBox");
        AboutPane ap = new AboutPane();
        f.getContentPane().add(ap);
        f.setResizable(false); // 크기 조절 불가
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // x 버튼 누르면 꺼짐.
        f.pack(); // 넣은 컴포넌트들에 맞춰서 사이즈 조정
        f.setLocationRelativeTo(null); // 중앙에 프레임 뜨게
        f.setVisible(true); // 화면 보임.
    }
    class NorthPane extends JPanel{
        NorthPane(){          
            JLabel label = new JLabel("About ChessProject",JLabel.LEFT);
            label.setFont(new Font(label.getFont().getName(),Font.BOLD,15));
            label.setForeground(Color.decode("#9900AF"));
            add(label);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = this.getWidth()-5;
            int height = this.getHeight() - 1;
            g.setColor(Color.decode("#9900FF"));
            g.drawLine(0, height, width, height);
        }
    }
}
