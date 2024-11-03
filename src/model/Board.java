package model;

import java.util.List;

public abstract class Board {
    public Board() {}

    public abstract void display();

    public abstract void build(String FEN);

    public abstract String generateFEN();

    public abstract void makeMove(Move move, boolean isBlue);

    public abstract void unmakeMove(Move move, boolean isBlue);

    public abstract void cleanBoard();

    public abstract List<Move> computeAllMoves(boolean isBlue);

    public abstract List<Move> generateSacrificingMoves(boolean isBlue);

    public abstract List<Move> generateQuietMoves(boolean isBlue);

    public abstract List<Move> generateMixedMoves(boolean isBlue);

    public abstract int distances(boolean isBlue);

    public abstract int cols(boolean isBlue);

    public abstract int wallsNumber(boolean isBlue);

    public abstract int towersNumber(boolean isBlue);

    public abstract int gameState(boolean isBlue);

    public abstract boolean isInCheck(boolean isBlue);

    public abstract boolean isInLosingPos(boolean isBlue);

}
