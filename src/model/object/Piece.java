package model.object;

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

}
