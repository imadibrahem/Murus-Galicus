package model.object;

import model.Move;

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

    public Move quietFront(){
        if (isBlue){

        }
        else {
            
        }
        return null;
    }

    public Move quietFrontRight(){
        return null;
    }

    public Move quietRight(){
        return null;
    }

    public Move quietBackRight(){
        return null;
    }

    public Move quietBack(){
        return null;
    }

    public Move quietBackLeft(){
        return null;
    }

    public Move quietLeft(){
        return null;
    }

    public Move quietFrontLeft(){
        return null;
    }

    public Move sacrificingFront(){
        return null;
    }

    public Move sacrificingFrontRight(){
        return null;
    }

    public Move sacrificingRight(){
        return null;
    }

    public Move sacrificingBackRight(){
        return null;
    }

    public Move sacrificingBack(){
        return null;
    }

    public Move sacrificingBackLeft(){
        return null;
    }

    public Move sacrificingLeft(){
        return null;
    }

    public Move sacrificingFrontLeft(){
        return null;
    }

}
