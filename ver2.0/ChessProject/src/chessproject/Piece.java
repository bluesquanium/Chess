/* check(1)
 * public Piece clone() ������ ��!
 * 
 */
package chessproject;


public class Piece {
    public int id; // �� ���� ��ġ, � ������
    public int location; // �� ��ġ
    public boolean has_moved; // ���� ���������� üũ
    //�̵����� ĭ
    //�̵�����
    
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
    public Piece clone(){ // Ŭ���� ������ ��
        return new Piece(id,location,has_moved);
    }
}
