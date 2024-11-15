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

        System.out.println("allTypeMovesPieceByPiece from front to back");
        System.out.println(allTypeMovesPieceByPiece(isBlue));
        allStyles.add(allTypeMovesPieceByPiece(isBlue));
        System.out.println();
        switchFrontToBack();
        System.out.println("allTypeMovesPieceByPiece from back to front");
        System.out.println(allTypeMovesPieceByPiece(isBlue));
        allStyles.add(allTypeMovesPieceByPiece(isBlue));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("typeByTypeMovesPieceByPiece from front to back");
        System.out.println(typeByTypeMovesPieceByPiece(isBlue));
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue));
        System.out.println();
        switchFrontToBack();
        System.out.println("typeByTypeMovesPieceByPiece from back to front");
        System.out.println(typeByTypeMovesPieceByPiece(isBlue));
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("directionByDirectionMovesPieceByPiece from front to back");
        System.out.println(directionByDirectionMovesPieceByPiece(isBlue));
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue));
        System.out.println();
        switchFrontToBack();
        System.out.println("directionByDirectionMovesPieceByPiece from back to front");
        System.out.println(directionByDirectionMovesPieceByPiece(isBlue));
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("allTypeMovesDirectionByDirection from front to back");
        System.out.println(allTypeMovesDirectionByDirection(isBlue));
        allStyles.add(allTypeMovesDirectionByDirection(isBlue));
        System.out.println();
        switchFrontToBack();
        System.out.println("allTypeMovesDirectionByDirection from back to front");
        System.out.println(allTypeMovesDirectionByDirection(isBlue));
        allStyles.add(allTypeMovesDirectionByDirection(isBlue));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("typeByTypeMovesDirectionByDirection from front to back");
        System.out.println(typeByTypeMovesDirectionByDirection(isBlue));
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue));
        System.out.println();
        switchFrontToBack();
        System.out.println("typeByTypeMovesDirectionByDirection from back to front");
        System.out.println(typeByTypeMovesDirectionByDirection(isBlue));
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("directionByDirectionMovesTypeByType from front to back");
        allStyles.add(directionByDirectionMovesTypeByType(isBlue));
        System.out.println();
        switchFrontToBack();
        System.out.println("directionByDirectionMovesTypeByType from back to front");
        System.out.println(directionByDirectionMovesTypeByType(isBlue));
        allStyles.add(directionByDirectionMovesTypeByType(isBlue));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        return allStyles;
    }

}
