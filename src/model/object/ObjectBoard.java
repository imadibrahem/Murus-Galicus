package model.object;

import model.Board;
import model.move.Move;
import model.move.MoveType;

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
        boolean nearEmpty = false;
        boolean farEmpty = false;

        Square initial = squares[move.getInitialLocation(isBlue)];
        Square targetNear = initial.singleMoveSquare(move.getDirection(), isBlue);
        Piece upper = initial.getUpperPiece();
        Piece lower = initial.getPiece();
        lower.setTopped(false);
        getArmy(isBlue).getTopped().remove(lower);
        initial.setUpperPiece(null);
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
                nearEmpty = true;
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
                //getArmy(isBlue).getTowers().add(upper);
            }
            if (move.isTargetEmpty() || move.isTargetNearFriendly()){
                farEmpty = true;
                targetFar.setPiece(lower);
                getArmy(isBlue).getWalls().add(lower);
            }
            else {
                Piece target = targetFar.getPiece();
                target.setTopped(true);
                lower.setTower(true);
                targetFar.setUpperPiece(lower);
                lower.setSquare(targetFar);
                getArmy(isBlue).getTowers().add(lower);
                getArmy(isBlue).getWalls().remove(lower);
                getArmy(isBlue).getWalls().remove(target);
                getArmy(isBlue).getTopped().add(target);
            }
            initial.setPiece(null);
            int type;
            if (farEmpty&&nearEmpty) type = 0;
            else if (!farEmpty&&!nearEmpty) type = 3;
            else if (farEmpty) type = 1;
            else type = 2;
            //System.out.println("+++++++++++++++++++++++++");
            //System.out.println("+++++++++++++++++++++++++");
            //System.out.println("*************************");
            System.out.println("make Move type was: " + type);
            ///System.out.println("*************************");
            //System.out.println("+++++++++++++++++++++++++");
            //System.out.println("+++++++++++++++++++++++++");
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
    public List<Move> generateSacrificingMoves(boolean isBlue) {
        return null;// TODO: 11/14/2024
    }

    @Override
    public List<Move> generateQuietMoves(boolean isBlue) {
        return null;// TODO: 11/14/2024
    }

    @Override
    public List<Move> generateMixedMoves(boolean isBlue) {
        return null;// TODO: 11/14/2024
    }

    @Override
    public int towersDistances(boolean isBlue, int[] values) {
        return getArmy(isBlue).towersDistances(values);
    }

    @Override
    public int wallsDistances(boolean isBlue, int[] values) {
        return getArmy(isBlue).wallsDistances(values);
    }

    @Override
    public int towersColumns(boolean isBlue,int[] values) {
        return getArmy(isBlue).towersColumns(values);
    }

    @Override
    public int wallsColumns(boolean isBlue,int[] values) {
        return getArmy(isBlue).wallsColumns(values);
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
    public boolean isInCheck(boolean isBlue) {
        if (generateMoves(isBlue).size() < 2) return true;
        for (Piece tower : getArmy(!isBlue).getTowers()){
            int location = isBlue ? tower.getSquare().getLocation() : 55 - tower.getSquare().getLocation();
            if (location > 31) return true;
        }
        return false;
    }

    @Override
    public boolean isInLosingPos(boolean isBlue) {
        if (generateMoves(isBlue).size() < 2) return true;
        List<Move> possibleMoves = new ArrayList<>();
        for (Piece tower : getArmy(!isBlue).getTowers()){
            int location = isBlue ? tower.getSquare().getLocation() : 55 - tower.getSquare().getLocation();
            if (location > 31) {
                if (location < 40)possibleMoves.addAll(allTypeMovesForOnePiece(new MoveType[]{MoveType.QUIET, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.FRIEND_ON_BOTH}
                        ,tower.getSquare().getLocation() ,new int[]{1,2,8}));
                else if (location < 48)possibleMoves.addAll(sacrificingMovesForOnePiece(tower.getSquare().getLocation(), new int[]{1,2,8}));
            }
        }
        return !(possibleMoves.isEmpty());
    }

    @Override
    public boolean lostGame(boolean isBlue) {
        if (generateMoves(isBlue).size() < 1) return true;
        for (Piece tower : getArmy(!isBlue).getTowers()){
            int location = isBlue ? tower.getSquare().getLocation() : 55 - tower.getSquare().getLocation();
            if (location > 47) return true;
        }
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
    public List<Short> normalMovesLocations(boolean isBlue, int location, int startDirection, boolean clockwise) {
        List<Short> normalMovesLocations = new ArrayList<>();
        int currentDirection = startDirection;
        if (this.isFriendlyTower(isBlue, location)){
            Piece piece = squares[location].getUpperPiece();
            for (int i = 1; i < 9; i++){
                if (piece.quietMove(currentDirection) != null||piece.friendOnNearMove(currentDirection)!= null||piece.friendOnFarMove(currentDirection) != null||piece.friendOnBothMove(currentDirection) != null){
                    Square[] doubleMoveSquares = squares[location].doubleMoveSquares(currentDirection,isBlue);
                    normalMovesLocations.add(doubleMoveSquares[0].getLocation());
                    normalMovesLocations.add(doubleMoveSquares[1].getLocation());
                }
                currentDirection = (currentDirection % 8) + 1;
            }
        }
        return normalMovesLocations;
    }

    @Override
    public List<Short> sacrificingMovesLocations(boolean isBlue, int location, int startDirection, boolean clockwise) {
        List<Short> sacrificingMovesLocations = new ArrayList<>();
        int currentDirection = startDirection;
        if (this.isFriendlyTower(isBlue, location)){
            Piece piece = squares[location].getUpperPiece();
            for (int i = 1; i < 9; i++){
                if (piece.sacrificingMove(currentDirection) != null){
                    sacrificingMovesLocations.add(squares[location].singleMoveSquare(currentDirection, isBlue).getLocation());
                }
                if (clockwise) currentDirection = (currentDirection % 8) + 1;
                else currentDirection = (currentDirection - 6) % 8 + 1;
            }
        }

        return sacrificingMovesLocations;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<Move> allTypeMovesPieceByPiece (boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        List<Move> allTypeMovesPieceByPiece = new ArrayList<>();
        if (frontToBack) {
            getArmy(isBlue).towersFromFrontToBack();
        } else {
            getArmy(isBlue).towersFromBackToFront();
        }
        for (Piece piece : getArmy(isBlue).getTowers()){
            allTypeMovesPieceByPiece.addAll(allTypeMovesForOnePiece(moveTypes, piece.getSquare().getLocation(),directions));
        }
        return allTypeMovesPieceByPiece;

    }

    @Override
    public List<Move> typeByTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        List<Move> typeByTypeMovesPieceByPiece = new ArrayList<>();
        for (MoveType moveType : moveTypes)typeByTypeMovesPieceByPiece.addAll(oneTypeMovesPieceByPiece(isBlue,moveType,directions,frontToBack));
        return typeByTypeMovesPieceByPiece;
    }

    @Override
    public List<Move> directionByDirectionMovesPieceByPiece(boolean isBlue,MoveType[] moveTypes, int[] directions, boolean frontToBack){
        List<Move> directionByDirectionMovesPieceByPiece = new ArrayList<>();
        if (frontToBack) {
            getArmy(isBlue).towersFromFrontToBack();
        } else {
            getArmy(isBlue).towersFromBackToFront();
        }
        for (Piece piece : getArmy(isBlue).getTowers()){
            directionByDirectionMovesPieceByPiece.addAll(directionByDirectionForOnePiece(moveTypes, piece.getSquare().getLocation(),directions));
        }
        return directionByDirectionMovesPieceByPiece;
    }

    @Override
    public List<Move> allTypeMovesDirectionByDirection (boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        List<Move> allTypeMovesDirectionByDirection = new ArrayList<>();
        for (int direction : directions){
            allTypeMovesDirectionByDirection.addAll(allTypeMovesForOneDirection(isBlue, moveTypes, direction, frontToBack));
        }
        return allTypeMovesDirectionByDirection;
    }

    @Override
    public List<Move> typeByTypeMovesDirectionByDirection(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        List<Move> typeByTypeMovesDirectionByDirection = new ArrayList<>();
        for (int direction : directions){
            for (MoveType moveType : moveTypes){
                typeByTypeMovesDirectionByDirection.addAll(oneTypeMovesForOneDirection(moveType,isBlue,direction,frontToBack));
            }
        }
        return typeByTypeMovesDirectionByDirection;

    }

    @Override
    public List<Move> directionByDirectionMovesTypeByType(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        List<Move> directionByDirectionMovesTypeByType = new ArrayList<>();
        for (MoveType moveType : moveTypes){
            for (int direction : directions){
                directionByDirectionMovesTypeByType.addAll(oneTypeMovesForOneDirection(moveType,isBlue,direction,frontToBack));
            }
        }
        return directionByDirectionMovesTypeByType;

    }

////////////////////////////////////////////////////////////////////////////////////

    public Square[] getSquares() {
        return squares;
    }

    public Army getArmy(boolean isBlue) {
        return isBlue ? blueArmy : redArmy;
    }


    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////

    public List<Move> targetEmptyForOnePiece(int location, int[] directions) {
        List<Move> targetEmptyForOnePiece = new ArrayList<>();
        Piece piece = squares[location].getUpperPiece();
        for (int direction : directions) {
            if (piece.quietMove(direction) != null) targetEmptyForOnePiece.add(piece.quietMove(direction));
        }
        return targetEmptyForOnePiece;
    }

    public List<Move> friendOnNearMoveForOnePiece(int location, int[] directions) {
        List<Move> friendOnNearMoveForOnePiece = new ArrayList<>();
        Piece piece = squares[location].getUpperPiece();
        for (int direction : directions) {
            if (piece.friendOnNearMove(direction) != null)
                friendOnNearMoveForOnePiece.add(piece.friendOnNearMove(direction));
        }
        return friendOnNearMoveForOnePiece;
    }

    public List<Move> friendOnFarMoveForOnePiece(int location, int[] directions) {
        List<Move> friendOnNearMoveForOnePiece = new ArrayList<>();
        Piece piece = squares[location].getUpperPiece();
        for (int direction : directions) {
            if (piece.friendOnNearMove(direction) != null)
                friendOnNearMoveForOnePiece.add(piece.friendOnNearMove(direction));
        }
        return friendOnNearMoveForOnePiece;
    }

    public List<Move> friendOnBothMoveForOnePiece(int location, int[] directions) {
        List<Move> friendOnBothMoveForOnePiece = new ArrayList<>();
        Piece piece = squares[location].getUpperPiece();
        for (int direction : directions) {
            if (piece.friendOnBothMove(direction) != null)
                friendOnBothMoveForOnePiece.add(piece.friendOnBothMove(direction));
        }
        return friendOnBothMoveForOnePiece;
    }

    public List<Move> sacrificingMovesForOnePiece(int location, int[] directions) {
        List<Move> sacrificingMoves = new ArrayList<>();
        Piece piece = squares[location].getUpperPiece();
        for (int direction : directions) {
            if (piece.sacrificingMove(direction) != null) sacrificingMoves.add(piece.sacrificingMove(direction));
        }
        return sacrificingMoves;
    }
    //////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////

    public List<Move> oneTypeMovesForOnePiece(MoveType moveType, int location, int[] directions) {
        if (moveType == MoveType.QUIET) return targetEmptyForOnePiece(location, directions);
        else if (moveType == MoveType.FRIEND_ON_NEAR) return friendOnNearMoveForOnePiece(location, directions);
        else if (moveType == MoveType.FRIEND_ON_FAR) return friendOnFarMoveForOnePiece(location, directions);
        else if (moveType == MoveType.FRIEND_ON_BOTH) return friendOnBothMoveForOnePiece(location, directions);
        else return sacrificingMovesForOnePiece(location, directions);
    }

    public Move oneTypeMoveForOneDirection(MoveType moveType, int location, int direction){
        Piece piece = squares[location].getUpperPiece();
        if (moveType == MoveType.QUIET) return piece.quietMove(direction);
        else if (moveType == MoveType.FRIEND_ON_NEAR) return piece.friendOnNearMove(direction);
        else if (moveType == MoveType.FRIEND_ON_FAR) return piece.friendOnFarMove(direction);
        else if (moveType == MoveType.FRIEND_ON_BOTH) return piece.friendOnBothMove(direction);
        else return piece.sacrificingMove(direction);
    }

    public Move moveForOneDirection(MoveType[] moveTypes, int location, int direction){
        Piece piece = squares[location].getUpperPiece();
        if (piece.quietMove(direction) != null) return piece.quietMove(direction);
        else if (piece.friendOnNearMove(direction) != null) return piece.friendOnNearMove(direction);
        else if (piece.friendOnFarMove(direction) != null) return piece.friendOnFarMove(direction);
        else if (piece.friendOnBothMove(direction) != null) return piece.friendOnBothMove(direction);
        else if (piece.sacrificingMove(direction) != null) return piece.sacrificingMove(direction);
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////

    public List<Move> allTypeMovesForOnePiece(MoveType[] moveTypes, int location, int[] directions) {
        List<Move> allTypeMovesForOnePiece = new ArrayList<>();
        for (MoveType moveType : moveTypes) allTypeMovesForOnePiece.addAll(oneTypeMovesForOnePiece(moveType,location,directions));
        return allTypeMovesForOnePiece;
    }

    public List<Move> oneTypeMovesPieceByPiece(boolean isBlue, MoveType moveType, int[] directions,  boolean frontToBack) {
        List<Move> oneTypeMovesLocationsPieceByPiece = new ArrayList<>();
        if (frontToBack) {
            getArmy(isBlue).towersFromFrontToBack();
        } else {
            getArmy(isBlue).towersFromBackToFront();
        }
        for (Piece piece : getArmy(isBlue).getTowers()){
            oneTypeMovesLocationsPieceByPiece.addAll(oneTypeMovesForOnePiece(moveType, piece.getSquare().getLocation(),directions));
        }
        return oneTypeMovesLocationsPieceByPiece;
    }

    public List<Move> directionByDirectionForOnePiece(MoveType[] moveTypes, int location, int[] directions) {
        List<Move> directionByDirectionForOnePiece = new ArrayList<>();
        for (int direction : directions){
            for (MoveType moveType : moveTypes){
                if (oneTypeMoveForOneDirection(moveType, location, direction) != null)directionByDirectionForOnePiece.add(oneTypeMoveForOneDirection(moveType, location, direction));
            }
        }
        return directionByDirectionForOnePiece;
    }

    public List<Move> allTypeMovesForOneDirection(boolean isBlue, MoveType[] moveTypes, int direction, boolean frontToBack){
        List<Move> allTypeMovesForOneDirection = new ArrayList<>();
        if (frontToBack) {
            getArmy(isBlue).towersFromFrontToBack();
        } else {
            getArmy(isBlue).towersFromBackToFront();
        }
        for (Piece piece : getArmy(isBlue).getTowers()){
            for (MoveType moveType : moveTypes){
                if (oneTypeMoveForOneDirection(moveType, piece.getSquare().getLocation(), direction) != null)allTypeMovesForOneDirection.add(oneTypeMoveForOneDirection(moveType, piece.getSquare().getLocation(), direction));

            }
        }
        return allTypeMovesForOneDirection;
    }

    public List<Move> oneTypeMovesForOneDirection(MoveType moveType, boolean isBlue, int direction, boolean frontToBack){
        List<Move> oneTypeMovesForOneDirection = new ArrayList<>();
        if (frontToBack) {
            getArmy(isBlue).towersFromFrontToBack();
        } else {
            getArmy(isBlue).towersFromBackToFront();
        }
        for (Piece piece : getArmy(isBlue).getTowers()){
            if (oneTypeMoveForOneDirection(moveType, piece.getSquare().getLocation(),direction) != null)oneTypeMovesForOneDirection.add(oneTypeMoveForOneDirection(moveType, piece.getSquare().getLocation(),direction));
        }
        return oneTypeMovesForOneDirection;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
}
