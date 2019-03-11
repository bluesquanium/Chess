/* check(1)
 * checkby** �Լ��� ������ �� ���� �ʾ����� ���� �� �˾���!
 * 
 */
package chessproject;


public class Game {	// king�� ��ġ�� ������ ��������, ���� ������ �� �ִ��� üũ�ϴµ� �߿��ϱ⿡ ��� ������ ����.
    ChessLocation chesslocation;
    Piece player1_king;
    Piece player2_king;
    
    public Game(ChessLocation chesslocation){
        player1_king = chesslocation.player1_pieces[8];
        player2_king = chesslocation.player2_pieces[8];
        this.chesslocation = chesslocation;
    }
    public int getResult(int player){
        int state = -1;
        MoveGenerator mg = new MoveGenerator(chesslocation,player);
        mg.generateMoves();
        ChessLocation[] chesslocations = mg.getChessLocations();
        if(chesslocations.length == 0){ // �� ��� üũ����Ʈ�ų� ��ο���
            if(isChecked(player)) {
                state = GameData.CHECKMATE;
            }
            else state = GameData.DRAW;
        }
        return state;
    }    
    public boolean safeMove(int player, int source,int destination){ // ���� �������� �� üũ���� �ƴ��� Ȯ��.
        Move _move = new Move(source,destination); // ������ ���� ��ġ�� ������ ��ġ ����
        ChessLocation _chesslocation = new ChessLocation(chesslocation,_move);  // �� ������ ���� ������Ʈ �ؼ� _chesslocation�� ����.
        Game gs = new Game(_chesslocation);   
        return !gs.isChecked(player); // ���� �������� �� üũ���� �ƴ��� Ȯ��. (üũ�̸� �� �����̸� �ȵ�)
    }
    public boolean isChecked(int player){ // ���� ���°� üũ���� �ƴ��� Ȯ���ϴ� ��.
        boolean checked = false;
        Piece king = (player == GameData.PLAYER1)?player1_king:player2_king;
        if(king == null) return false;
        checked = checkedByPawn(king);
        if(!checked) checked = checkedByKnight(king);
        if(!checked) checked = checkedByBishop(king);
        if(!checked) checked = checkedByRook(king);
        if(!checked) checked = checkedByQueen(king);
        if(!checked) checked = desSquareAttackedByKing(king);       
        return checked;
    }
    private boolean checkedByPawn(Piece king){
        boolean checked = false;   
        int location = king.location;
        if(king == player1_king){
            int right_square = chesslocation.chessboard[location-9];
            int left_square = chesslocation.chessboard[location-11];
            if(right_square == GameData.IMPOSSIBLE || left_square == GameData.IMPOSSIBLE) return false;
            if(right_square<0 && chesslocation.player2_pieces[-right_square].id == GameData.PAWN)
                checked = true;
            if(left_square<0 && chesslocation.player2_pieces[-left_square].id == GameData.PAWN)
                checked = true;
        }else{
            int right_square = chesslocation.chessboard[location+11];
            int left_square = chesslocation.chessboard[location+9];
            if(right_square != GameData.IMPOSSIBLE){
                if(right_square>0 && right_square != GameData.NOTHING && 
                        chesslocation.player1_pieces[right_square].id == GameData.PAWN)
                    checked = true;
            }
            if(left_square != GameData.IMPOSSIBLE){
                if(left_square>0 && left_square != GameData.NOTHING && 
                        chesslocation.player1_pieces[left_square].id == GameData.PAWN)
                    checked = true;
            }
        }
        return checked;
    }
    private boolean checkedByKnight(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location-21,location+21,location+19,location-19,
            location-12,location+12,location-8,location+8};
        for(int destination:destinations){
        	
        	if(destination>=chesslocation.chessboard.length) continue;
        	if(destination<0) continue;
        	
            int des_square = chesslocation.chessboard[destination];
            if(des_square == GameData.IMPOSSIBLE) continue;
            if(king == player1_king){                
                if(des_square<0 && chesslocation.player2_pieces[-des_square].id == GameData.KNIGHT){
                    checked = true;
                    break;
                }
            }else{
                if(des_square>0 && des_square != GameData.NOTHING && 
                        chesslocation.player1_pieces[des_square].id == GameData.KNIGHT){
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }
    private boolean desSquareAttackedByKing(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location+1,location-1,location+10,location-10,
            location+11,location-11,location+9,location-9};
        for(int destination:destinations){
        	
        	if(destination>=chesslocation.chessboard.length) continue;
        	if(destination<0) continue;
        	
            int des_square = chesslocation.chessboard[destination];
            if(des_square == GameData.IMPOSSIBLE) continue;
            if(king == player1_king){                
                if(des_square<0 && chesslocation.player2_pieces[-des_square].id == GameData.KING){
                    checked = true;
                    break;
                }
            }else{
                if(des_square>0 && des_square != GameData.NOTHING && 
                        chesslocation.player1_pieces[des_square].id == GameData.KING){
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }
    private boolean checkedByBishop(Piece king){
        boolean checked = false;
        int[] deltas = {11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            
            if(delta >= chesslocation.chessboard.length) continue;
            if(delta < 0) continue;
            
            while(true){
                int des_square = chesslocation.chessboard[delta];
                if(des_square == GameData.IMPOSSIBLE) {
                    checked = false;
                    break;
                }
                if(king == player1_king){
                    if(des_square<0 && chesslocation.player2_pieces[-des_square].id == GameData.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != GameData.NOTHING) break;
                }else if(king == player2_king){
                    if(des_square>0 && des_square != GameData.NOTHING && 
                            chesslocation.player1_pieces[des_square].id == GameData.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != GameData.NOTHING) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }    
    private boolean checkedByRook(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            
            if(delta >= chesslocation.chessboard.length) continue;
            if(delta < 0) continue;
            
            while(true){
                int des_square = chesslocation.chessboard[delta];
                if(des_square == GameData.IMPOSSIBLE) {
                    checked = false;
                    break;
                }
                if(king == player1_king){
                    if(des_square<0 && chesslocation.player2_pieces[-des_square].id == GameData.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != GameData.NOTHING) break;
                }else if(king == player2_king){
                    if(des_square>0 && des_square != GameData.NOTHING && 
                            chesslocation.player1_pieces[des_square].id == GameData.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != GameData.NOTHING) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }    
    private boolean checkedByQueen(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10,11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            
            if(delta >= chesslocation.chessboard.length) continue;
            if(delta < 0) continue;
            
            while(true){
                int des_square = chesslocation.chessboard[delta];
                if(des_square == GameData.IMPOSSIBLE) {
                    checked = false;
                    break;
                }
                if(king == player1_king){
                    if(des_square<0 && chesslocation.player2_pieces[-des_square].id == GameData.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != GameData.NOTHING) break;
                }else if(king == player2_king){
                    if(des_square>0 && des_square != GameData.NOTHING && 
                            chesslocation.player1_pieces[des_square].id == GameData.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != GameData.NOTHING) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }
}
