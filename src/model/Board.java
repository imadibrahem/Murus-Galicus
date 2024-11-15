package model;

import model.move.Move;
import model.move.MoveType;

import java.util.List;

public abstract class Board {
    public Board() {}

    public abstract void build(String FEN);

    public abstract String generateFEN();

    public abstract void makeMove(Move move, boolean isBlue);

    public abstract void unmakeMove(Move move, boolean isBlue);

    public abstract void cleanBoard();

    public abstract List<Move> computeAllMoves(boolean isBlue);

    public abstract List<Move> generateSacrificingMoves(boolean isBlue);

    public abstract List<Move> generateQuietMoves(boolean isBlue);

    public abstract List<Move> generateMixedMoves(boolean isBlue);

    public abstract int towersDistances(boolean isBlue);

    public abstract int wallsDistances(boolean isBlue);

    public abstract int towersColumns(boolean isBlue);

    public abstract int wallsColumns(boolean isBlue);

    public abstract int wallsNumber(boolean isBlue);

    public abstract int towersNumber(boolean isBlue);

    public abstract int gameState(boolean isBlue);

    public abstract boolean isInCheck(boolean isBlue);

    public abstract boolean isInLosingPos(boolean isBlue);

    public abstract boolean isFriendlyTower(boolean isBlue, int location);

    public abstract boolean isFriendlyPiece(boolean isBlue, int location);

    public abstract List<Short> normalMovesLocations (boolean isBlue, int location, int startDirection, boolean clockwise);

    public abstract List<Short> sacrificingMovesLocations (boolean isBlue, int location, int startDirection, boolean clockwise);

    public abstract List<Move>  allTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack);

    public abstract List<Move> typeByTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack);

    public abstract List<Move> directionByDirectionMovesPieceByPiece(boolean isBlue,MoveType[] moveTypes, int[] directions, boolean frontToBack);

    public abstract List<Move> allTypeMovesDirectionByDirection(boolean isBlue,MoveType[] moveTypes, int[] directions, boolean frontToBack);

    public abstract List<Move> typeByTypeMovesDirectionByDirection(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack);

    public abstract List<Move> directionByDirectionMovesTypeByType(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack);
}
