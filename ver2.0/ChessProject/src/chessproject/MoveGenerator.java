/* check(1)
 * ������ �� �ִ� ĭ���� �������� ���� �� �ִ� chesslocation���� List<ChessLocation> chesslocations�� �����ϴ� ������ ����.
 * 
 */
package chessproject;

import java.util.ArrayList;
import java.util.List;


public class MoveGenerator {    
    ChessLocation chesslocation;
    int player;
    List<ChessLocation> chesslocations = new ArrayList<ChessLocation>();   
    Game gs;
    
    public MoveGenerator(ChessLocation chesslocation, int player){
        this.chesslocation = chesslocation;
        this.player = player;
        this.gs = new Game(chesslocation);
    }
    public ChessLocation[] getChessLocations(){ // List<ChessLocation> chesslocations�� �迭�� ��ȯ���ִ� �Լ�    
        return chesslocations.toArray(new ChessLocation[chesslocations.size()]);
    }
    public void generateMoves(){
        if(player == GameData.PLAYER1){
            for(int i=1; i<chesslocation.player1_pieces.length; i++){                
                Piece piece = chesslocation.player1_pieces[i];       
                if(piece == null) continue;
                switch(piece.id){
                    case GameData.PAWN:
                        player1PawnMoves(piece);
                        break;
                    case GameData.KNIGHT:
                        player1KnightMoves(piece);
                        break;
                    case GameData.KING:
                        player1KingMoves(piece);
                        break;
                    case GameData.BISHOP:
                        player1BishopMoves(piece);
                        break;
                    case GameData.ROOK:
                        player1RookMoves(piece);
                        break;
                    case GameData.QUEEN:
                        player1QueenMoves(piece);
                }
            }
        }else{
            for(int i=1; i<chesslocation.player2_pieces.length; i++){
                Piece piece = chesslocation.player2_pieces[i];    
                if(piece == null) continue;
                switch(piece.id){
                    case GameData.PAWN:
                        player2PawnMoves(piece);
                        break;
                    case GameData.KNIGHT:
                        player2KnightMoves(piece);
                        break;
                    case GameData.KING:
                        player2KingMoves(piece);
                        break;
                    case GameData.BISHOP:
                        player2BishopMoves(piece);
                        break;
                    case GameData.ROOK:
                        player2RookMoves(piece);
                        break;
                    case GameData.QUEEN:
                        player2QueenMoves(piece);
                }
            }
        }
    }
    public void player1PawnMoves(Piece pawn){        
        int location = pawn.location;
        int forward_piece_index = chesslocation.chessboard[location-10]; // ���� ��ĭ. board��  ���� 10ĭ �����Ƿ�. (���� 12ĭ)
        if(forward_piece_index != GameData.IMPOSSIBLE){ // ���� �ٱ� �ƴ� ��
            if(forward_piece_index == GameData.NOTHING && gs.safeMove(GameData.PLAYER1, location, location-10)) { // �ش� ĭ�� ��������� + (�� ���� üũ�� ���� �ʴ� ������ ĭ. �� �κ��� ��� �ش�ǹǷ� �Ʒ����ʹ� ������.)
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-10))); // �����ǿ��� ��ĭ �� �����ΰ� ����
            }
        }
        
        if(location > 80 && forward_piece_index == GameData.NOTHING && 
                chesslocation.chessboard[location-20] == GameData.NOTHING && gs.safeMove(GameData.PLAYER1,location,location-20)) { // �� �ѹ��� �� ��������, ��ĭ ��, ��ĭ �� ��� ��ĭ�̸�            
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-20)));
        }
        
        int right_piece_index = chesslocation.chessboard[location-9]; // ������ �� Ȯ��
        if(right_piece_index != GameData.IMPOSSIBLE) { // ���� �ٱ� �ƴ� ��
            if(right_piece_index<0 && gs.safeMove(GameData.PLAYER1, location,location-9)) // ��ǻ���� ���� ��
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-9)));
        }
        int left_piece_index = chesslocation.chessboard[location-11]; // ���� �� Ȯ��
        if(left_piece_index != GameData.IMPOSSIBLE) { // ���� �ٱ� �ƴ� ��
            if(left_piece_index<0 && gs.safeMove(GameData.PLAYER1, location,location-11)) // ��ǻ���� ���� ��
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-11)));
        }        
    }    
    public void player2PawnMoves(Piece pawn){      
        int location = pawn.location;
        int forward_piece_index = chesslocation.chessboard[location+10];
        if(forward_piece_index != GameData.IMPOSSIBLE){
            if(forward_piece_index == GameData.NOTHING && gs.safeMove(GameData.PLAYER2, location,location+10)){ 
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location+10)));
            }
        }
        
        if(location < 39 && forward_piece_index == GameData.NOTHING && 
                chesslocation.chessboard[location+20] == GameData.NOTHING && gs.safeMove(GameData.PLAYER2, location,location+20)) {            
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location+20)));
        }
        
        int right_piece_index = chesslocation.chessboard[location+11];
        if(right_piece_index != GameData.IMPOSSIBLE) {
            if(right_piece_index>0 && right_piece_index != GameData.NOTHING &&
                    gs.safeMove(GameData.PLAYER2, location,location+11))
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location+11)));
        }
        int left_piece_index = chesslocation.chessboard[location+9];
        if(left_piece_index != GameData.IMPOSSIBLE) {
            if(left_piece_index>0 && left_piece_index != GameData.NOTHING &&
                    gs.safeMove(GameData.PLAYER2, location,location+9))
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location+9)));
        }        
    }    
    public void player1KnightMoves(Piece knight){
        int location = knight.location;
        int[] destinations = {location-21,location+21,location+19,location-19,
            location-12,location+12,location-8,location+8}; // ������ �� �ִ� ĭ��
        for(int i=0; i<destinations.length; i++){ // ������ �� �ִ� ĭ�� ��ĭ �� Ȯ���غ���
        	
        	if(destinations[i]>=chesslocation.chessboard.length) continue;
        	if(destinations[i]<0) continue;
        	
            int des_piece_index = chesslocation.chessboard[destinations[i]]; // �ش� ������ �� �ִ� ĭ��
            if(des_piece_index != GameData.IMPOSSIBLE && (des_piece_index == GameData.NOTHING || des_piece_index<0) // ���� �ٱ� �ƴϰ�, ��ĭ�̰ų� ��ǻ�� ���� �� 
                     && gs.safeMove(GameData.PLAYER1, location,destinations[i]))
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,destinations[i])));
        }
    }
    public void player2KnightMoves(Piece knight){
        int location = knight.location;
        int[] destinations = {location-21,location+21,location+19,location-19,
            location-12,location+12,location-8,location+8};
        for(int i=0; i<destinations.length; i++){
        	
        	if(destinations[i]>=chesslocation.chessboard.length) continue;
        	if(destinations[i]<0) continue;
        	
        	if(destinations[i]<0) continue;
            int des_piece_index = chesslocation.chessboard[destinations[i]];
            if(des_piece_index != GameData.IMPOSSIBLE && (des_piece_index == GameData.NOTHING || des_piece_index>0) &&
                    gs.safeMove(GameData.PLAYER2, location,destinations[i])){
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,destinations[i])));
            }
        }
    }
    public void player1KingMoves(Piece king){
        int location = king.location;
        int[] destinations = {location+1,location-1,location+10,location-10, // ������ �� �ִ� ĭ��
            location+11,location-11,location+9,location-9};
        for(int i=0; i<destinations.length; i++){ // ������ �� �ִ� ĭ�� ��ĭ�� Ȯ���غ���
        	
        	if(destinations[i]>=chesslocation.chessboard.length) continue;
        	if(destinations[i]<0) continue;
        	
            int des_piece_index = chesslocation.chessboard[destinations[i]]; // �ش� ������ �� �ִ� ĭ��
            if(des_piece_index != GameData.IMPOSSIBLE && (des_piece_index == GameData.NOTHING || des_piece_index<0) // ���� �ٱ� �ƴϰ� ��ĭ�̰ų� ��ǻ�� ���� ��
                    && gs.safeMove(GameData.PLAYER1, location,destinations[i])){
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,destinations[i])));
            }
        }
    }
    public void player2KingMoves(Piece king){
        int location = king.location;
        int[] destinations = {location+1,location-1,location+10,location-10,
            location+11,location-11,location+9,location-9};
        for(int i=0; i<destinations.length; i++){
        	
        	if(destinations[i]>=chesslocation.chessboard.length) continue;
        	if(destinations[i]<0) continue;
        	
            int des_piece_index = chesslocation.chessboard[destinations[i]];
            if(des_piece_index != GameData.IMPOSSIBLE && (des_piece_index == GameData.NOTHING || des_piece_index>0) &&
                    gs.safeMove(GameData.PLAYER2, location,destinations[i])){
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,destinations[i])));
            }
        }
    }
    public void player1BishopMoves(Piece bishop){
        int location = bishop.location;
        int[] deltas = {11,-11,9,-9}; // ������ �� �ִ� ���� ����
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) { // ������ �� �ִ� ���� ��ĭ�� �÷����� Ȯ��
                int des_piece_index = chesslocation.chessboard[des]; // ������ �� �ִ� ĭ ����
                if (des_piece_index == GameData.IMPOSSIBLE) { // ���� ���� ���������� while���� ������
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER1, location,des); // �ش� ���갡 ��������(üũ�ƴ���) ����
                if (des_piece_index == GameData.NOTHING || des_piece_index < 0){ // �ش� ��ġ ����ְų� ��ǻ�� ���̸�
                    if(safe_move){ // �����ϸ�
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING || !safe_move) { // !safe_move�� ���� ��. �ش� ��ġ ��ǻ�� ���̸� while���� ����.                        
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break; // �������� ���� �� �ش���ġ ��ǻ�� ���� �ƴ϶�� while���� ����.
                } else if(des_piece_index>0 && des_piece_index != GameData.NOTHING){ // �ش���ġ ���� ���̸�
                    break;
                }
                des += deltas[i];
            }
        }
    }
    public void player2BishopMoves(Piece bishop){
        int location = bishop.location;
        int[] deltas = {11,-11,9,-9};
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) {
                int des_piece_index = chesslocation.chessboard[des];
                if (des_piece_index == GameData.IMPOSSIBLE) {
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER2, location,des);
                if (des_piece_index == GameData.NOTHING || des_piece_index > 0) {
                    if(safe_move){
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING || !safe_move) {                        
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break;
                } else if(des_piece_index<0){
                    break;
                }
                des += deltas[i];
            }
        }
    }
    public void player1RookMoves(Piece rook){
        int location = rook.location;
        int[] deltas = {1,-1,10,-10};
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) {
                int des_piece_index = chesslocation.chessboard[des];
                if (des_piece_index == GameData.IMPOSSIBLE) {
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER1, location,des);
                if (des_piece_index == GameData.NOTHING || des_piece_index < 0) {
                    if(safe_move){
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING) {          
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break;
                } else if(des_piece_index>0 && des_piece_index != GameData.NOTHING){
                    break;
                }
                des += deltas[i];
            }
        }
    }
    public void player2RookMoves(Piece rook){
        int location = rook.location;
        int[] deltas = {1,-1,10,-10};
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) {
                 int des_piece_index = chesslocation.chessboard[des];
                if (des_piece_index == GameData.IMPOSSIBLE) {
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER2, location,des);
                if (des_piece_index == GameData.NOTHING || des_piece_index > 0) {
                    if(safe_move){
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING) {                        
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break;
                } else if(des_piece_index<0){
                    break;
                }
                des += deltas[i];
            }
        }
    }
    public void player1QueenMoves(Piece queen){
        int location = queen.location;
        int[] deltas = {1,-1,10,-10,11,-11,9,-9};
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) {
                int des_piece_index = chesslocation.chessboard[des];
                if (des_piece_index == GameData.IMPOSSIBLE) {
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER1, location,des);
                if (des_piece_index == GameData.NOTHING || des_piece_index < 0) {
                    if(safe_move){
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING) {                        
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break;
                } else if(des_piece_index>0 && des_piece_index != GameData.NOTHING){
                    break;
                }
                des += deltas[i];
            }
        }
    }
    public void player2QueenMoves(Piece queen){
        int location = queen.location;
        int[] deltas = {1,-1,10,-10,11,-11,9,-9};
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) {
                int des_piece_index = chesslocation.chessboard[des];
                if (des_piece_index == GameData.IMPOSSIBLE) {
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER2, location,des);
                if (des_piece_index == GameData.NOTHING || des_piece_index > 0) {
                    if(safe_move){
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING) {                        
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break;
                } else if(des_piece_index<0){
                    break;
                }
                des += deltas[i];
            }
        }
    }
}
