/* check(1)
 * 
 */
package chessproject;


public class ChessLocation {
    Move move_last;
    public int[] chessboard;// 이거 new int[120] 할거임
    Piece[] player1_pieces = new Piece[17];
    Piece[] player2_pieces = new Piece[17];
    
    public ChessLocation(){
    	chessboard = new int[120];
        for(int i=0; i<chessboard.length; i++){
            chessboard[i] = GameData.NOTHING;
        }
    }
    public ChessLocation(ChessLocation chesslocation){
        this(chesslocation,null);
    }
    public ChessLocation(ChessLocation chesslocation, Move move_last){
    	chessboard = new int[120];
    	
    	for(int i = 0; i<chesslocation.chessboard.length; i++) {
    		this.chessboard[i] = chesslocation.chessboard[i];
    	}
    	
        for(int i=1; i<player1_pieces.length; i++){
            if(chesslocation.player1_pieces[i] != null){
                this.player1_pieces[i] = chesslocation.player1_pieces[i].clone();
            }
            if(chesslocation.player2_pieces[i] != null){
                this.player2_pieces[i] = chesslocation.player2_pieces[i].clone();
            }
        }
        if(move_last != null) update(move_last);
    }
    
    public void initialize(boolean humanWhite){         
        player1_pieces[1] = new Piece(GameData.KNIGHT,92);
        player1_pieces[2] = new Piece(GameData.KNIGHT,97);
        player1_pieces[3] = new Piece(GameData.BISHOP,93);
        player1_pieces[4] = new Piece(GameData.BISHOP,96);
        player1_pieces[5] = new Piece(GameData.ROOK,91);
        player1_pieces[6] = new Piece(GameData.ROOK,98);
        player1_pieces[7] = new Piece(GameData.QUEEN,humanWhite?94:95);
        player1_pieces[8] = new Piece(GameData.KING,humanWhite?95:94);
        
        player2_pieces[1] = new Piece(GameData.KNIGHT,22);
        player2_pieces[2] = new Piece(GameData.KNIGHT,27);
        player2_pieces[3] = new Piece(GameData.BISHOP,23);
        player2_pieces[4] = new Piece(GameData.BISHOP,26);
        player2_pieces[5] = new Piece(GameData.ROOK,21);
        player2_pieces[6] = new Piece(GameData.ROOK,28);
        player2_pieces[7] = new Piece(GameData.QUEEN,humanWhite?24:25);
        player2_pieces[8] = new Piece(GameData.KING,humanWhite?25:24); 
        
        int j = 81;
        for(int i=9; i<player1_pieces.length; i++){
            player1_pieces[i] = new Piece(GameData.PAWN,j);
            player2_pieces[i] = new Piece(GameData.PAWN,j-50);
            j++;
        }                      
        chessboard = new int[]{
            GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.NOTHING,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,
            GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE,GameData.IMPOSSIBLE
        };        
        for(int i=0; i<chessboard.length; i++){                        
            for(int k=1; k<player1_pieces.length; k++){
                if(i==player1_pieces[k].location){
                    chessboard[i] = k;
                }else if(i==player2_pieces[k].location){
                    chessboard[i] = -k;
                }
            }
        }
    }    
    public void update(Move move){
        this.move_last = move;   
        int source_index = chessboard[move.source_location];
        int destination_index = chessboard[move.destination];  
        if(source_index>0){
            player1_pieces[source_index].has_moved = true;
            player1_pieces[source_index].location = move.destination;
            if(destination_index<0){                
                player2_pieces[-destination_index] = null;
            }            
        }else{
            player2_pieces[-source_index].has_moved = true;
            player2_pieces[-source_index].location = move.destination;
            if(destination_index>0 && destination_index != GameData.NOTHING){ // 양수는 꼭 플레이어의 말이 아님.                
                player1_pieces[destination_index] = null;
            }            
        }
        chessboard[move.source_location] = GameData.NOTHING;
        chessboard[move.destination] = source_index;
    }
}
