package chessproject;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/* check(1)
 * properties ������ �����ϴ� Ŭ����.
 */

public class Resource {
    protected static ResourceBundle resources;
    static { // static�� -> public Resource() �� �ٲ㵵��. 
        try{
            resources = ResourceBundle.getBundle("chessproject.res.ChessProjectProperties",Locale.getDefault());
        }catch(Exception e){
            System.out.println("ChessProjectProperties�� ã�� �� �����ϴ�!");
            javax.swing.JOptionPane.showMessageDialog(null, // â���� �����޽��� ���.
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
