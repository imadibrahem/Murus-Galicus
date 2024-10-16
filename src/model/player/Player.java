package model.player;

import model.Board;
import model.Move;

public abstract class Player {
    private boolean isBlue;
    private final Board board;
    protected int winScore;
    protected int loseScore = -winScore;

    public Player(boolean isBlue,Board board) {
        this.isBlue = isBlue;
        this.board = board;
    }

    public boolean isBlue() {
        return isBlue;
    }

    public Board getBoard() {
        return board;
    }

    public void switchColor() {
        isBlue = !isBlue;
    }

    public abstract Move decideMove();

    public void makeMove(Move move){
        board.makeMove(move);
    }

    public void unmakeMove(Move move){
        board.unmakeMove(move);
    }
}
