/* check(1)
 * 움직일 수 있는 칸들을 움직여서 나올 수 있는 chesslocation들을 List<ChessLocation> chesslocations에 저장하는 것으로 보임.
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
    public ChessLocation[] getChessLocations(){ // List<ChessLocation> chesslocations를 배열로 반환해주는 함수    
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
        int forward_piece_index = chesslocation.chessboard[location-10]; // 폰의 앞칸. board가  가로 10칸 있으므로. (세로 12칸)
        if(forward_piece_index != GameData.IMPOSSIBLE){ // 보드 바깥 아닐 때
            if(forward_piece_index == GameData.NOTHING && gs.safeMove(GameData.PLAYER1, location, location-10)) { // 해당 칸이 비어있으면 + (내 왕이 체크가 되지 않는 안전한 칸. 이 부분은 계속 해당되므로 아래부터는 생략함.)
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-10))); // 포지션에서 한칸 앞 움직인거 저장
            }
        }
        
        if(location > 80 && forward_piece_index == GameData.NOTHING && 
                chesslocation.chessboard[location-20] == GameData.NOTHING && gs.safeMove(GameData.PLAYER1,location,location-20)) { // 폰 한번도 안 움직였고, 한칸 앞, 두칸 앞 모두 빈칸이면            
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-20)));
        }
        
        int right_piece_index = chesslocation.chessboard[location-9]; // 오른쪽 위 확인
        if(right_piece_index != GameData.IMPOSSIBLE) { // 보드 바깥 아닐 때
            if(right_piece_index<0 && gs.safeMove(GameData.PLAYER1, location,location-9)) // 컴퓨터의 말일 때
                chesslocations.add(new ChessLocation(chesslocation,new Move(location,location-9)));
        }
        int left_piece_index = chesslocation.chessboard[location-11]; // 왼쪽 위 확인
        if(left_piece_index != GameData.IMPOSSIBLE) { // 보드 바깥 아닐 때
            if(left_piece_index<0 && gs.safeMove(GameData.PLAYER1, location,location-11)) // 컴퓨터의 말일 때
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
            location-12,location+12,location-8,location+8}; // 움직일 수 있는 칸들
        for(int i=0; i<destinations.length; i++){ // 움직일 수 있는 칸들 한칸 씩 확인해보기
        	
        	if(destinations[i]>=chesslocation.chessboard.length) continue;
        	if(destinations[i]<0) continue;
        	
            int des_piece_index = chesslocation.chessboard[destinations[i]]; // 해당 움직일 수 있는 칸이
            if(des_piece_index != GameData.IMPOSSIBLE && (des_piece_index == GameData.NOTHING || des_piece_index<0) // 보드 바깥 아니고, 빈칸이거나 컴퓨터 말일 때 
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
        int[] destinations = {location+1,location-1,location+10,location-10, // 움직일 수 있는 칸들
            location+11,location-11,location+9,location-9};
        for(int i=0; i<destinations.length; i++){ // 움직일 수 있는 칸들 한칸씩 확인해보기
        	
        	if(destinations[i]>=chesslocation.chessboard.length) continue;
        	if(destinations[i]<0) continue;
        	
            int des_piece_index = chesslocation.chessboard[destinations[i]]; // 해당 움직일 수 있는 칸이
            if(des_piece_index != GameData.IMPOSSIBLE && (des_piece_index == GameData.NOTHING || des_piece_index<0) // 보드 바깥 아니고 빈칸이거나 컴퓨터 말일 때
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
        int[] deltas = {11,-11,9,-9}; // 움직일 수 있는 기울기 저장
        for (int i = 0; i < deltas.length; i++) {
            int des = location + deltas[i];
            
            if(des>=chesslocation.chessboard.length) continue;
            if(des<0) continue;
            
            while (true) { // 움직일 수 있는 기울기 한칸씩 늘려가며 확인
                int des_piece_index = chesslocation.chessboard[des]; // 움직일 수 있는 칸 저장
                if (des_piece_index == GameData.IMPOSSIBLE) { // 보드 끝에 도달했으면 while루프 끝내기
                    break;
                }
                boolean safe_move = gs.safeMove(GameData.PLAYER1, location,des); // 해당 무브가 안전한지(체크아닌지) 저장
                if (des_piece_index == GameData.NOTHING || des_piece_index < 0){ // 해당 위치 비어있거나 컴퓨터 말이면
                    if(safe_move){ // 안전하면
                        chesslocations.add(new ChessLocation(chesslocation,new Move(location,des)));
                        if (des_piece_index != GameData.NOTHING || !safe_move) { // !safe_move는 빼도 됨. 해당 위치 컴퓨터 말이면 while루프 끝냄.                        
                            break;
                        }
                    }else if(des_piece_index != GameData.NOTHING) break; // 안전하지 않을 때 해당위치 컴퓨터 말이 아니라면 while루프 끝냄.
                } else if(des_piece_index>0 && des_piece_index != GameData.NOTHING){ // 해당위치 내꺼 말이면
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
