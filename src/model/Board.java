package model;

import java.util.List;

public abstract class Board {
    public Board() {}

    public abstract void display();

    public abstract void build(String FEN);

    public abstract void makeMove(Move move);

    public abstract void unmakeMove(Move move);

    public abstract void cleanBoard();

    public abstract List<Move> computeAllMoves(boolean isBlue);

    public abstract List<Move> generateCapturingMoves(boolean isBlue);

    public abstract int distances(boolean isBlue);

    public abstract int cols(boolean isBlue);

    public abstract int wallsNumber(boolean isBlue);

    public abstract int towersNumber(boolean isBlue);

    public abstract int gameState(boolean isBlue);

    public abstract long getWalls(boolean isBlue);

    public abstract long getTowers(boolean isBlue);

    public abstract boolean isInCheck(boolean isBlue);

    public abstract boolean isInLosingPos(boolean isBlue);
}
