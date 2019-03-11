/* check(1)
 * public Piece clone() 복사할 때!
 * 
 */
package chessproject;


public class Piece {
    public int id; // 이 말의 가치, 어떤 말인지
    public int location; // 말 위치
    public boolean has_moved; // 말이 움직였는지 체크
    //이동가능 칸
    //이동방향
    
    public Piece(int _id, int _location){
        id = _id;
        location = _location;
        has_moved = false;
    }
    public Piece(int _id, int _location, boolean _has_moved){
        id = _id;
        location = _location;
        has_moved = _has_moved;
    }
    
    @Override
    public Piece clone(){ // 클래스 복사할 때
        return new Piece(id,location,has_moved);
    }
}
