/* check(1)
 * 여기는 게임 진행에 쓰이는 용어들 값 저장해놓음.
 * 
 */
package chessproject;


public class GameData {
	//Piece
    public final static int PAWN = 100;
    public final static int KNIGHT = 300;
    public final static int BISHOP = 350;
    public final static int ROOK = 500;
    public final static int QUEEN = 900;
    public final static int KING = 100000;       	
	
	//플레이어
    public final static int PLAYER1 = 1; // 플레이어1 HUMAN -> PLAYER1
    public final static int PLAYER2 = 0; // 플레이어2 COMPUTER -> PLAYER2
    
    // 빈칸/불가능
    public final static int NOTHING = 44;
    public final static int IMPOSSIBLE = 66;
    
    // state
    public final static int PLAYER1_MOVE = 1000;
    public final static int PLAYER2_MOVE = 1001;
    public final static int PREPARE_MOVING = 1002;
    public final static int MOVING = 1003;
    public final static int GAME_END = 1004;
    
    //게임 끝내기 관련
    public final static int DRAW = 0;
    public final static int CHECKMATE = 1;
    
    //보드
    public final static int CHESSBOARD_MAIN = 2000;
    public final static int CHESSBOARD_HISTORY = 2001;
    public final static int TITLE_HISTORY = 2002;
    public final static int TITLE_CHESSPROJECT = 2003;
    
    // click/move tile
    public final static int CLICK_TILE = 2004;
    public final static int MOVE_TILE = 2005;
    
    //버튼
    public final static int BUTTON_LOCALGAME = 10002;
    public final static int BUTTON_LOCALGAME_R = 10003;
    public final static int BUTTON_NETWORKGAME = 10004;
    public final static int BUTTON_NETWORKGAME_R = 10005;
    public final static int BUTTON_HISTORY = 10006;
    public final static int BUTTON_HISTORY_R = 10007;
    public final static int BUTTON_ABOUT = 10008;
    public final static int BUTTON_ABOUT_R = 10009;
    public final static int BUTTON_QUIT = 10010;
    public final static int BUTTON_QUIT_R = 10011;
    
    public final static int BUTTON_FIRST = 10012;
    public final static int BUTTON_FIRST_R = 10013;
    public final static int BUTTON_LAST = 10014;
    public final static int BUTTON_LAST_R = 10015;
    public final static int BUTTON_PREV = 10016;
    public final static int BUTTON_PREV_R = 10017;
    public final static int BUTTON_NEXT = 10018;
    public final static int BUTTON_NEXT_R = 10019;
    
    //기보 저장 수
    public final static int NUM_OF_HISTORY = 10;
    
    //Network 관련
    public final static int MESSENGER_HOST_PORT = 5000;
    public final static int MESSENGER_CLIENT_PORT = 6000;
    public final static int NETWORKGAME_HOST_PORT = 5001;
    public final static int NETWORKGAME_CLIENT_PORT = 6001;
    
    public final static int PROMOTION = 20000;
    
    //버전, 업데이트
    public final static String AUTHOR = new Resource().getResourceString("author");
    public final static String CATEGORY = new Resource().getResourceString("category");
    public final static String VERSION = new Resource().getResourceString("version");
    public final static String UPDATED_DATE = new Resource().getResourceString("date");

}
