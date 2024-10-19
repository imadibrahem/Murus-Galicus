package model.object;

public class Square {

    private final ObjectBoard board;
    private int row;
    private int col;
    private final byte location;
    private Piece piece;
    private String name;

    public Square(ObjectBoard board, byte location) {
        this.board = board;
        this.location = location;
        this.piece = null;
        setRow();
        setCol();
        setName();
    }

    public ObjectBoard getBoard() {
        return board;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }
    
    public byte getLocation() {
        return location;
    }
    
    public String getName() {
        return name;
    }
    
    public void emptySquare(){
        this.piece = null;
    }

    public void setCol() {
        this.col = location - ((row - 1) * 8) ;
    }

    public void setRow() {
        this.row = ((location + 1) / 8) + 1;
    }

    public void setName() {
        this.name = ((char) row + 48) + "" + ((char) col + 64);
    }
    
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public static void main (String[] args){
        System.out.println((char) 65);
        System.out.println((char) 48);
    }

}
