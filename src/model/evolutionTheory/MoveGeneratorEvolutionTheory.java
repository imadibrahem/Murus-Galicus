package model.evolutionTheory;
import model.Board;
import model.move.Move;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveType;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorEvolutionTheory extends MoveGenerator {
    private Board board;
    private MoveGeneratingStyle style;
    private MoveType[] moveTypes;
    private int[] directions;
    private boolean frontToBack;

    public MoveGeneratorEvolutionTheory(Board board, MoveGeneratingStyle style, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        this.board = board;
        this.style = style;
        this.moveTypes = moveTypes;
        this.directions = directions;
        this.frontToBack = frontToBack;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public MoveGeneratingStyle getStyle() {
        return style;
    }

    public void setStyle(MoveGeneratingStyle style) {
        this.style = style;
    }

    public MoveType[] getMoveTypes() {
        return moveTypes;
    }

    public void setMoveTypes(MoveType[] moveTypes) {
        this.moveTypes = moveTypes;
    }

    public int[] getDirections() {
        return directions;
    }

    public void setDirections(int[] directions) {
        this.directions = directions;
    }

    public boolean isFrontToBack() {
        return frontToBack;
    }

    public void setFrontToBack(boolean frontToBack) {
        this.frontToBack = frontToBack;
    }

    public void switchFrontToBack() {
        frontToBack = !frontToBack;

    }


    public List<Move> generateMoves (boolean isBlue){
        if (this.style == MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE) return allTypeMovesPieceByPiece(isBlue);
        if (this.style == MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_PIECE_BY_PIECE) return typeByTypeMovesPieceByPiece(isBlue);
        if (this.style == MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE) return directionByDirectionMovesPieceByPiece(isBlue);
        if (this.style == MoveGeneratingStyle.ALL_TYPE_MOVES_DIRECTION_BY_DIRECTION) return allTypeMovesDirectionByDirection(isBlue);
        if (this.style == MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION) return typeByTypeMovesDirectionByDirection(isBlue);
        else return directionByDirectionMovesTypeByType(isBlue);
    }

    public List<Move> allTypeMovesPieceByPiece(boolean isBlue){
        return board.allTypeMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> typeByTypeMovesPieceByPiece(boolean isBlue){
        return board.typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> directionByDirectionMovesPieceByPiece(boolean isBlue){
        return board.directionByDirectionMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> allTypeMovesDirectionByDirection (boolean isBlue){
        return board.allTypeMovesDirectionByDirection(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> typeByTypeMovesDirectionByDirection(boolean isBlue){
        return board.typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> directionByDirectionMovesTypeByType(boolean isBlue ){
        return board.directionByDirectionMovesTypeByType(isBlue, moveTypes, directions, frontToBack);
    }

    public List<List<Move>> generateAllStyles (boolean isBlue){
        List<List<Move>> allStyles = new ArrayList<>();

        allStyles.add(allTypeMovesPieceByPiece(isBlue));
        switchFrontToBack();
        allStyles.add(allTypeMovesPieceByPiece(isBlue));

        allStyles.add(typeByTypeMovesPieceByPiece(isBlue));
        switchFrontToBack();
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue));

        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue));
        switchFrontToBack();
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue));

        allStyles.add(allTypeMovesDirectionByDirection(isBlue));
        switchFrontToBack();
        allStyles.add(allTypeMovesDirectionByDirection(isBlue));

        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue));
        switchFrontToBack();
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue));

        allStyles.add(directionByDirectionMovesTypeByType(isBlue));
        switchFrontToBack();
        allStyles.add(directionByDirectionMovesTypeByType(isBlue));

        return allStyles;
    }

}
