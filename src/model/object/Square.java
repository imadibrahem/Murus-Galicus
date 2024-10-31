package model.object;

public class Square {

    private final ObjectBoard board;
    private int row;
    private int col;
    private final short location;
    private Piece piece;
    private Piece upperPiece;
    private String name;

    public Square(ObjectBoard board, short location) {
        this.board = board;
        this.location = location;
        this.piece = null;
        this.upperPiece = null;
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
    
    public short getLocation() {
        return location;
    }
    
    public String getName() {
        return name;
    }

    public Piece getUpperPiece() {
        return upperPiece;
    }

    public void setCol() {
        this.col = (location % 8) + 1;
    }

    public void setRow() {
        this.row = 7 - (location / 8);
    }

    public void setName() {
        this.name =  ((char) ('A' + (col - 1))) + Integer.toString(row);
    }
    
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setUpperPiece(Piece upperPiece) {
        this.upperPiece = upperPiece;
    }

    public Square singleMoveSquare(int direction, boolean isBlue){

        int squareLoc = -1;
        boolean rightValid = !((isBlue && (location + 1) % 8 == 0) || (!isBlue && (location % 8 == 0)));
        boolean leftValid = !((!isBlue && (location + 1) % 8 == 0) || (isBlue && (location % 8 == 0)));

        if (direction == 1) squareLoc = isBlue ? location - 8 : location + 8;

        else if (direction == 2 && rightValid) squareLoc = isBlue ? location - 7 : location + 7;

        else if (direction == 3 && rightValid) squareLoc = isBlue ? location + 1 : location - 1;

        else if (direction == 4 && rightValid) squareLoc = isBlue ? location + 9 : location - 9;

        else if (direction == 5) squareLoc = isBlue ? location + 8 : location - 8;

        else if (direction == 6 && leftValid) squareLoc = isBlue ? location + 7 : location - 7;

        else if (direction == 7 && leftValid) squareLoc = isBlue ? location - 1 : location + 1;

        else if (direction == 8 && leftValid) squareLoc = isBlue ? location - 9 : location + 9;

        return (squareLoc > -1 && squareLoc < 56) ?  board.getSquares()[squareLoc] :  null;
    }

    public Square[] doubleMoveSquares(int direction, boolean isBlue){
        Square[] doubleMoveSquares = new Square[2];
        doubleMoveSquares[0] = singleMoveSquare(direction, isBlue);
        doubleMoveSquares[1] = doubleMoveSquares[0] == null ? null : doubleMoveSquares[0].singleMoveSquare(direction, isBlue);
        return doubleMoveSquares;
    }

    public static void main (String[] args){
        ObjectBoard board = new ObjectBoard();
        for (Square square : board.getSquares()){
            System.out.println("Location: " + square.getLocation());
            System.out.println("Row:      " + square.getRow());
            System.out.println("Column:   " + square.getCol());
            System.out.println("Name:     " + square.getName());
            System.out.println("/////////////////////");
        }

        Square initial = board.getSquares()[30];
        Square resultBlue;
        Square resultRed;
        String blue;
        String red;
        for (int i = 1; i < 9; i++){
            resultBlue = initial.singleMoveSquare(i, true);
            resultRed = initial.singleMoveSquare(i, false);
            blue =  resultBlue == null ? null : String.valueOf(resultBlue.getLocation());
            red =  resultRed == null ? null : String.valueOf(resultRed.getLocation());

            System.out.println("/////////////////////");
            System.out.println("Blue: " + initial.getLocation() + " -> " + i +" : " + blue);
            System.out.println("Red : " + initial.getLocation() + " -> " + i +" : "+ red);
            System.out.println("/////////////////////");

        }

        Square[] blueResults;
        Square[] redResults;
        for (int i = 1; i < 9; i++){
            blueResults = initial.doubleMoveSquares(i, true);
            redResults = initial.doubleMoveSquares(i, false);
            blue = blueResults[0] == null ? null : String.valueOf(blueResults[0].getLocation());
            blue = blueResults[1] == null ? null : blue + " - " + (blueResults[1].getLocation());
            red = redResults[0] == null ? null : String.valueOf(redResults[0].getLocation());
            red = redResults[1] == null ? null : red + " - " + (redResults[1].getLocation());

            System.out.println("/////////////////////");
            System.out.println("Blue: " + initial.getLocation() + " -> " + i +" : " + blue);
            System.out.println("Red : " + initial.getLocation() + " -> " + i +" : "+ red);
            System.out.println("/////////////////////");
        }


    }

}
