package model.object;

import model.Board;
import model.Move;

import java.util.List;

public class ObjectBoard extends Board {

    private final Square[] squares;
    private final Army blueArmy;
    private final Army redArmy;

    public ObjectBoard() {
        this.squares = new Square[56];
        this.blueArmy = new Army(true);
        this.redArmy = new Army(false);
        for (int i = 0; i < 56; i++){
            squares[i] = new Square(this, i);
        }
    }

    @Override
    public void display() {

    }

    @Override
    public void build(String FEN) {
        int squareCol;
        int squareLoc;
        Square square;
        String [] rows = FEN.split("[/\\s]+");
        for (int r = 0; r < 8; r++){
            squareCol = 0;
            for (int c = 0; c < rows[r].length(); c++){
                if (Character.isDigit(rows[r].charAt(c))){
                    squareCol += Character.getNumericValue(rows[r].charAt(c));
                }
                else {
                    squareCol++;
                    squareLoc = (r * 8) + (squareCol - 1);
                    square = this.squares[squareLoc];
                    Piece piece;
                    if (rows[r].charAt(c) == 'w'){
                        piece = this.redArmy.getSacrificedPieces().remove(0);
                        this.redArmy.getWalls().add(piece);
                    }
                    else if (rows[r].charAt(c) == 'W'){
                        piece = this.blueArmy.getSacrificedPieces().remove(0);
                        this.blueArmy.getWalls().add(piece);
                    }
                    else if (rows[r].charAt(c) == 't'){
                        piece = this.redArmy.getSacrificedPieces().remove(0);
                        this.redArmy.getTowers().add(piece);
                        piece.setTower(true);
                        piece.setSquare(square);
                        square.setUpperPiece(piece);
                        piece = this.redArmy.getSacrificedPieces().remove(0);
                        this.redArmy.getTopped().add(piece);
                        piece.setTopped(true);
                    }
                    else{
                        piece = this.blueArmy.getSacrificedPieces().remove(0);
                        this.blueArmy.getTowers().add(piece);
                        piece.setTower(true);
                        piece.setSquare(square);
                        square.setUpperPiece(piece);
                        piece = this.blueArmy.getSacrificedPieces().remove(0);
                        this.blueArmy.getTopped().add(piece);
                        piece.setTopped(true);
                    }
                    piece.setSquare(square);
                    square.setPiece(piece);

                }

            }

        }

    }

    @Override
    public void makeMove(Move move) {

    }

    @Override
    public void unmakeMove(Move move) {

    }

    @Override
    public void cleanBoard() {

    }

    @Override
    public List<Move> computeAllMoves(boolean isBlue) {
        return null;
    }

    @Override
    public List<Move> generateCapturingMoves(boolean isBlue) {
        return null;
    }

    @Override
    public int distances(boolean isBlue) {
        return 0;
    }

    @Override
    public int cols(boolean isBlue) {
        return 0;
    }

    @Override
    public int wallsNumber(boolean isBlue) {
        return 0;
    }

    @Override
    public int towersNumber(boolean isBlue) {
        return 0;
    }

    @Override
    public int gameState(boolean isBlue) {
        return 0;
    }

    @Override
    public long getWalls(boolean isBlue) {
        return 0;
    }

    @Override
    public long getTowers(boolean isBlue) {
        return 0;
    }

    @Override
    public boolean isInCheck(boolean isBlue) {
        return false;
    }

    @Override
    public boolean isInLosingPos(boolean isBlue) {
        return false;
    }

    @Override
    public String generateFEN() {
        return null;
    }
}
