package model.object;

import model.Board;
import model.Move;

import java.util.ArrayList;
import java.util.List;

public class ObjectBoard extends Board {

    private final Square[] squares;
    private final Army blueArmy;
    private final Army redArmy;
    private String FEN;

    public ObjectBoard(String FEN) {
        this.squares = new Square[56];
        this.blueArmy = new Army(true);
        this.redArmy = new Army(false);
        this.FEN = FEN;
        for (short i = 0; i < 56; i++){
            squares[i] = new Square(this, i);
        }
        build(FEN);
    }

    @Override
    public void build(String FEN) {
        cleanBoard();
        int squareCol;
        int squareLoc;
        Square square;
        String [] rows = FEN.split("[/\\s]+");

        for (int r = 0; r < 7; r++){
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
    public String generateFEN() {
        String result = "";
        int emptySquares;
        for (int i = 0; i < 7; i++){
            emptySquares = 0;
            for (int j = 0; j < 8; j++){
                if (squares[(i * 8) + j].getPiece() == null) emptySquares++;
                else {
                    if (emptySquares > 0){
                        result += emptySquares;
                        emptySquares = 0;
                    }
                    if (squares[(i * 8) + j].getUpperPiece() == null)result += squares[(i * 8) + j].getPiece().getSymbol();
                    else result += squares[(i * 8) + j].getUpperPiece().getSymbol();
                }
            }
            if (emptySquares > 0) result += emptySquares;
            result += "/";
        }
        return result;
    }


    @Override
    public void makeMove(Move move, boolean isBlue) {
        Square initial = squares[move.getInitialLocation(isBlue)];
        Square targetNear = initial.singleMoveSquare(move.getDirection(), isBlue);
        Piece upper = initial.getUpperPiece();
        Piece lower = initial.getPiece();
        lower.setTopped(false);
        initial.setUpperPiece(null);
        getArmy(isBlue).getTopped().remove(lower);
        if (move.isTargetEnemy()){
            Piece target = targetNear.getPiece();
            upper.setTower(false);
            upper.setSquare(null);
            target.setSquare(null);
            targetNear.setPiece(null);
            getArmy(!isBlue).getSacrificedPieces().add(target);
            getArmy(!isBlue).getWalls().remove(target);
            getArmy(isBlue).getTowers().remove(upper);
            getArmy(isBlue).getSacrificedPieces().add(upper);
            getArmy(isBlue).getWalls().add(lower);
        }
        else{
            Square targetFar = targetNear.singleMoveSquare(move.getDirection(), isBlue);
            upper.setSquare(targetNear);
            lower.setSquare(targetFar);
            if (move.isTargetEmpty() || move.isTargetFarFriendly()){
                upper.setTower(false);
                targetNear.setPiece(upper);
                getArmy(isBlue).getTowers().remove(upper);
                getArmy(isBlue).getWalls().add(upper);
            }
            else {
                Piece target = targetNear.getPiece();
                target.setTopped(true);
                targetNear.setUpperPiece(upper);
                getArmy(isBlue).getWalls().remove(target);
                getArmy(isBlue).getTopped().add(target);
            }
            if (move.isTargetEmpty() || move.isTargetNearFriendly()){
                targetFar.setPiece(lower);
                getArmy(isBlue).getWalls().add(lower);
            }
            else {
                Piece target = targetNear.getUpperPiece();
                target.setTopped(true);
                targetFar.setUpperPiece(lower);
                getArmy(isBlue).getTowers().add(lower);
                getArmy(isBlue).getWalls().remove(target);
                getArmy(isBlue).getTopped().add(target);
            }
            initial.setPiece(null);
        }
    }

    @Override
    public void unmakeMove(Move move, boolean isBlue) {
        Square initial = squares[move.getInitialLocation(isBlue)];
        Square targetNear = initial.singleMoveSquare(move.getDirection(), isBlue);
        Piece upper;
        Piece lower;
        Piece target;
        if (move.isTargetEnemy()){
            upper = getArmy(isBlue).getSacrificedPieces().remove(getArmy(isBlue).getSacrificedPieces().size() - 1);
            target = getArmy(!isBlue).getSacrificedPieces().remove(getArmy(!isBlue).getSacrificedPieces().size() -1);
            lower = initial.getPiece();
            upper.setTower(true);
            target.setSquare(targetNear);
            targetNear.setPiece(target);
            getArmy(isBlue).getTowers().add(upper);
            getArmy(!isBlue).getWalls().add(target);
            getArmy(isBlue).getWalls().remove(lower);
            getArmy(isBlue).getTopped().add(lower);
            initial.setUpperPiece(upper);
            upper.setSquare(initial);
            return;
        }
        else if (move.isTargetEmpty() || move.isTargetFarFriendly()){
            upper = targetNear.getPiece();
            targetNear.setPiece(null);
            getArmy(isBlue).getTowers().add(upper);
            getArmy(isBlue).getWalls().remove(upper);
            upper.setTower(true);
        }
        else {
            upper = targetNear.getUpperPiece();
            target = targetNear.getPiece();
            targetNear.setUpperPiece(null);
            getArmy(isBlue).getTopped().remove(target);
            getArmy(isBlue).getWalls().add(target);
            target.setTopped(false);
        }

        initial.setUpperPiece(upper);
        upper.setSquare(initial);
        Square targetFar = targetNear.singleMoveSquare(move.getDirection(), isBlue);

        if (move.isTargetEmpty() || move.isTargetNearFriendly()){
            lower = targetFar.getPiece();
            targetFar.setPiece(null);
            getArmy(isBlue).getWalls().remove(lower);
        }
        else {
            lower = targetFar.getUpperPiece();
            targetFar.setUpperPiece(null);
            lower.setTower(false);
            target = targetFar.getPiece();
            target.setTopped(false);
            getArmy(isBlue).getTowers().remove(lower);
        }
        lower.setTopped(true);
        getArmy(isBlue).getTopped().add(lower);
        initial.setPiece(lower);
        lower.setSquare(initial);
    }


    @Override
    public void cleanBoard() {
        redArmy.withdrawFromBoard();
        blueArmy.withdrawFromBoard();
    }

    @Override
    public List<Move> computeAllMoves(boolean isBlue) {
        // TODO: 11/5/2024 CHANGE LATER!!
        List<Piece> towers = getArmy(isBlue).getTowers();
        List<Move> moves = new ArrayList<>();
        for (Piece tower : towers){
            for (int direction = 1; direction < 9; direction++){

            }
        }
        return null;
    }

    @Override
    public List<Move> generateSacrificingMoves(boolean isBlue) {
        Army army = isBlue ? blueArmy : redArmy;
        return null;
    }

    @Override
    public List<Move> generateQuietMoves(boolean isBlue) {
        return null;
    }

    @Override
    public List<Move> generateMixedMoves(boolean isBlue) {
        return null;
    }

    @Override
    public int distances(boolean isBlue) {
        Army army = isBlue ? blueArmy : redArmy;
        // TODO: 10/31/2024  
        return 0;
    }

    @Override
    public int cols(boolean isBlue) {
        Army army = isBlue ? blueArmy : redArmy;
        // TODO: 10/31/2024  
        return 0;
    }

    @Override
    public int wallsNumber(boolean isBlue) {
        return getArmy(isBlue).getWalls().size();
    }

    @Override
    public int towersNumber(boolean isBlue) {
        return getArmy(isBlue).getTowers().size();
    }

    @Override
    public int gameState(boolean isBlue) {
        // TODO: 10/31/2024  
        return 0;
    }

    @Override
    public boolean isInCheck(boolean isBlue) {
        // TODO: 10/31/2024  
        return false;
    }

    @Override
    public boolean isInLosingPos(boolean isBlue) {
        // TODO: 10/31/2024  
        return false;
    }

    public boolean isFriendlyTower(boolean isBlue, int location) {
       return squares[location].getUpperPiece() != null && squares[location].getUpperPiece().isBlue() == isBlue;
    }

    @Override
    public boolean isFriendlyPiece(boolean isBlue, int location) {
        return squares[location].getPiece() != null && squares[location].getPiece().isBlue() == isBlue;
    }

    @Override
    public List<Short> normalMovesLocations(boolean isBlue, int location) {
        List<Short> normalMovesLocations = new ArrayList<>();
        if (this.isFriendlyTower(isBlue, location)){
            Piece piece = squares[location].getUpperPiece();
            for (int i = 1; i < 9; i++){
                if (piece.quietMove(i) != null||piece.friendOnNearMove(i)!= null||piece.friendOnFarMove(i) != null||piece.friendOnBothMove(i) != null){
                    Square[] doubleMoveSquares = squares[location].doubleMoveSquares(i,isBlue);
                    normalMovesLocations.add(doubleMoveSquares[0].getLocation());
                    normalMovesLocations.add(doubleMoveSquares[1].getLocation());

                }
            }
        }
        return normalMovesLocations;
    }

    @Override
    public List<Short> sacrificingMovesLocations(boolean isBlue, int location) {
        List<Short> sacrificingMovesLocations = new ArrayList<>();
        if (this.isFriendlyTower(isBlue, location)){
            Piece piece = squares[location].getUpperPiece();
            for (int i = 1; i < 9; i++){
                if (piece.sacrificingMove(i) != null){
                    sacrificingMovesLocations.add(squares[location].singleMoveSquare(i, isBlue).getLocation());
                }
            }
        }

        return sacrificingMovesLocations;
    }

    public Square[] getSquares() {
        return squares;
    }

    public Army getArmy(boolean isBlue) {
        return isBlue ? blueArmy : redArmy;
    }
}
