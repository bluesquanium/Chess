package chessproject;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/* check(1)
 * properties 파일을 관리하는 클래스.
 */

public class Resource {
    protected static ResourceBundle resources;
    static { // static을 -> public Resource() 로 바꿔도됨. 
        try{
            resources = ResourceBundle.getBundle("chessproject.res.ChessProjectProperties",Locale.getDefault());
        }catch(Exception e){
            System.out.println("ChessProjectProperties을 찾을 수 없습니다!");
            javax.swing.JOptionPane.showMessageDialog(null, // 창으로 에러메시지 띄움.
                    "Can't find ChessProjectProperties!",
                    "Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    public String getResourceString(String key){
        String str;
        try{
            str = resources.getString(key);
        }catch(Exception e){
            str = null;
        }
        return str;
    }
    protected URL getResource(String key){
        String name = getResourceString(key);
        if(name != null){
            URL url = this.getClass().getResource(name);
            return url;
        }
        return null;
    }
}
