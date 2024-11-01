package model.object;

import java.util.ArrayList;
import java.util.List;

public class Army {

    private final boolean isBlue;
    private final List<Piece> walls;
    private final List<Piece> towers;
    private final List<Piece> topped;
    private final List<Piece> sacrificedPieces;

    public Army(boolean isBlue) {
        this.isBlue = isBlue;
        this.walls = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.topped = new ArrayList<>();
        this.sacrificedPieces = new ArrayList<>();
        for (int i = 0; i < 16; i++) sacrificedPieces.add(new Piece(isBlue));
    }

    public boolean isBlue() {
        return isBlue;
    }

    public List<Piece> getWalls() {
        return walls;
    }

    public List<Piece> getTowers() {
        return towers;
    }

    public List<Piece> getTopped() {
        return topped;
    }

    public List<Piece> getSacrificedPieces() {
        return sacrificedPieces;
    }

    public void withdrawFromBoard() {
        withdrawTowers();
        withdrawWalls();
    }

    public void withdrawTowers(){
        Piece piece;
        Square square;
        for (Piece upperPiece : towers){
            square = upperPiece.getSquare();
            piece = square.getPiece();
            upperPiece.setTower(false);
            upperPiece.setSquare(null);
            piece.setTopped(false);
            piece.setSquare(null);
            square.setPiece(null);
            square.setUpperPiece(null);
        }
        sacrificedPieces.addAll(towers);
        sacrificedPieces.addAll(topped);
        towers.clear();
        topped.clear();

    }

    public void withdrawWalls(){
        Square square;
        for (Piece piece : walls){
            square = piece.getSquare();
            piece.setSquare(null);
            square.setPiece(null);
        }
        sacrificedPieces.addAll(walls);
        walls.clear();
    }


}
