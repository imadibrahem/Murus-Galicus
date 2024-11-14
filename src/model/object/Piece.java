package model.object;

import model.move.Move;

public class Piece {
    private final boolean isBlue;
    private boolean isTower;
    private boolean isTopped;
    private Square square;
    private char symbol;

    public Piece(boolean isBlue) {
        this.isBlue = isBlue;
        this.isTower = false;
        this.square = null;
        setSymbol();
    }

    public boolean isBlue() {
        return isBlue;
    }

    public boolean isTower() {
        return isTower;
    }

    public void setTower(boolean tower) {
        isTower = tower;
        setSymbol();
    }

    public boolean isTopped() {
        return isTopped;
    }

    public void setTopped(boolean topped) {
        isTopped = topped;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol() {
        this.symbol = isTower? 't' : 'w';
        if (isBlue) this.symbol = Character.toUpperCase(this.symbol);
    }

    public Move quietMove(int direction){
        Square[] targets = square.doubleMoveSquares(direction, isBlue);
        if (targets[1] != null && targets[0].getPiece() == null && targets[1].getPiece() == null){
            int location = isBlue ? square.getLocation() : 55 - square.getLocation();
            return new Move((short) (location << 7 | direction << 3));
        }
        return null;
    }

    public Move friendOnNearMove(int direction){
        Square[] targets = square.doubleMoveSquares(direction, isBlue);
        if (targets[1] != null && targets[0].getPiece() != null && targets[0].getUpperPiece() == null
                && targets[1].getPiece() == null && targets[0].getPiece().isBlue() == isBlue){
            int location = isBlue ? square.getLocation() : 55 - square.getLocation();
            return new Move((short) (location << 7 | direction << 3 | 1));
        }
        return null;
    }

    public Move friendOnFarMove(int direction){
        Square[] targets = square.doubleMoveSquares(direction, isBlue);
        if (targets[1] != null && targets[0].getPiece() == null && targets[1].getPiece() != null
                && targets[1].getUpperPiece() == null && targets[1].getPiece().isBlue() == isBlue){
            int location = isBlue ? square.getLocation() : 55 - square.getLocation();
            return new Move((short) (location << 7 | direction << 3 | 2));
        }
        return null;
    }

    public Move friendOnBothMove(int direction){
        Square[] targets = square.doubleMoveSquares(direction, isBlue);
        if (targets[1] != null && targets[0].getPiece() != null && targets[1].getPiece() != null
                && targets[0].getPiece().isBlue() == isBlue && targets[1].getPiece().isBlue() == isBlue
                && targets[1].getUpperPiece() == null && targets[0].getUpperPiece() == null){
            int location = isBlue ? square.getLocation() : 55 - square.getLocation();
            return new Move((short) (location << 7 | direction << 3 | 3));
        }
        return null;
    }

    public Move sacrificingMove(int direction){
        Square target = square.singleMoveSquare(direction, isBlue);
        if (target != null && target.getPiece() != null && target.getPiece().isBlue() != isBlue
                && target.getUpperPiece() == null){
            int location = isBlue ? square.getLocation() : 55 - square.getLocation();
            return new Move((short) (location << 7 | direction << 3 | 4));
        }
        return null;
    }

    @Override
    public String toString() {
        String loc = square == null ? "" : " ["+square.getLocation()+"]";
        return symbol + loc;
    }

}

