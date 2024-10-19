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
    }

    @Override
    public void display() {

    }

    @Override
    public void build(String FEN) {

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
