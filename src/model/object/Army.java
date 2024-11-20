package model.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Army {

    private final boolean isBlue;
    private final List<Piece> walls;
    private final List<Piece> towers;
    private final List<Piece> topped;
    private final List<Piece> sacrificedPieces;
    private final UpperToLowerBoardComparator upperToLowerBoardComparator;
    private final LowerToUpperBoardComparator lowerToUpperBoardComparator;

    public Army(boolean isBlue) {
        this.isBlue = isBlue;
        this.walls = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.topped = new ArrayList<>();
        this.sacrificedPieces = new ArrayList<>();
        this.upperToLowerBoardComparator = new UpperToLowerBoardComparator();
        this.lowerToUpperBoardComparator = new LowerToUpperBoardComparator();
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

    public int distanceFinder(int[] values, List<Piece> pieceList){
        int pieceLocation;
        int distances = 0;
        for (Piece piece : pieceList){
            pieceLocation = isBlue ? piece.getSquare().getLocation() : 55 - (piece.getSquare().getLocation());
            distances += values [6 - (pieceLocation / 8)];
        }
        return distances;
    }

    public int towersDistances(int[] values) {
        return distanceFinder(values, towers);
    }

    public int wallsDistances(int[] values) {
        return distanceFinder(values, walls);

    }
    public int columnsFinder(int[] values, List<Piece> pieceList){
        int pieceColumn;
        int columns = 0;
        for (Piece piece : pieceList){
            pieceColumn = (piece.getSquare().getLocation() % 8);
            if (pieceColumn > 3) pieceColumn = 7 - pieceColumn;
            columns += values[pieceColumn];
        }
        return columns;
    }
    public int towersColumns(int[] values) {
        return columnsFinder(values, towers);
    }

    public int wallsColumns(int[] values) {
        return columnsFinder(values, walls);
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

    public void towersFromFrontToBack(){
        if (isBlue) {
            towers.sort(upperToLowerBoardComparator);
        } else {
            towers.sort(lowerToUpperBoardComparator);
        }
    }

    public void towersFromBackToFront(){
        if (isBlue) {
            towers.sort(lowerToUpperBoardComparator);
        } else {
            towers.sort(upperToLowerBoardComparator);
        }
    }

    @Override
    public String toString() {
        String color = isBlue ? "Blue" : "Red";
        System.out.println("*************************");
        System.out.println("-------------------------");

        return  "-------------------------\n"
                +color + " Army: \n"+
                "walls: " + walls + "\n" +
                "towers :" + towers + "\n" +
                "topped: " + topped + "\n" +
                "sacrificedPieces: " + sacrificedPieces + "\n" +
                "-------------------------\n";
    }



    static class UpperToLowerBoardComparator implements java.util.Comparator<Piece> {
        @Override
        public int compare(Piece a, Piece b) {
            return a.getSquare().getLocation() - b.getSquare().getLocation();
        }
    }

    static class LowerToUpperBoardComparator implements java.util.Comparator<Piece> {
        @Override
        public int compare(Piece a, Piece b) {
            return b.getSquare().getLocation() - a.getSquare().getLocation();
        }
    }
}
