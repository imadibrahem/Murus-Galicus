package model.move;
import model.Board;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorEvolutionTheory {
    private Board board;
    private MoveGeneratingStyle style;
    MoveType[] moveTypes;
    int[] directions;

    public MoveGeneratorEvolutionTheory(Board board, MoveGeneratingStyle style, MoveType[] moveTypes, int[] directions) {
        this.board = board;
        this.style = style;
        this.moveTypes = moveTypes;
        this.directions = directions;
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

    public List<Move> generateMoves (boolean isBlue, boolean frontToBack){
        if (this.style == MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE) return allTypeMovesPieceByPiece(isBlue, frontToBack);
        if (this.style == MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_PIECE_BY_PIECE) return typeByTypeMovesPieceByPiece(isBlue, frontToBack);
        if (this.style == MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE) return directionByDirectionMovesPieceByPiece(isBlue, frontToBack);
        if (this.style == MoveGeneratingStyle.ALL_TYPE_MOVES_DIRECTION_BY_DIRECTION) return allTypeMovesDirectionByDirection(isBlue, frontToBack);
        if (this.style == MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION) return typeByTypeMovesDirectionByDirection(isBlue, frontToBack);
        else return directionByDirectionMovesTypeByType(isBlue, frontToBack);
    }

    public List<Move> allTypeMovesPieceByPiece(boolean isBlue, boolean frontToBack){
        return board.allTypeMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> typeByTypeMovesPieceByPiece(boolean isBlue, boolean frontToBack){
        return board.typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> directionByDirectionMovesPieceByPiece(boolean isBlue, boolean frontToBack){
        return board.directionByDirectionMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> allTypeMovesDirectionByDirection (boolean isBlue, boolean frontToBack){
        return board.allTypeMovesDirectionByDirection(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> typeByTypeMovesDirectionByDirection(boolean isBlue, boolean frontToBack){
        return board.typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> directionByDirectionMovesTypeByType(boolean isBlue, boolean frontToBack){
        return board.directionByDirectionMovesTypeByType(isBlue, moveTypes, directions, frontToBack);
    }

    public List<List<Move>> generateAllStyles (boolean isBlue){
        List<List<Move>> allStyles = new ArrayList<>();

        System.out.println("allTypeMovesPieceByPiece from front to back");
        System.out.println(allTypeMovesPieceByPiece(isBlue,true));
        allStyles.add(allTypeMovesPieceByPiece(isBlue,true));
        System.out.println();
        System.out.println("allTypeMovesPieceByPiece from back to front");
        System.out.println(allTypeMovesPieceByPiece(isBlue,false));
        allStyles.add(allTypeMovesPieceByPiece(isBlue,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("typeByTypeMovesPieceByPiece from front to back");
        System.out.println(typeByTypeMovesPieceByPiece(isBlue, true));
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue, true));
        System.out.println();
        System.out.println("typeByTypeMovesPieceByPiece from back to front");
        System.out.println(typeByTypeMovesPieceByPiece(isBlue, false));
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue, false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("directionByDirectionMovesPieceByPiece from front to back");
        System.out.println(directionByDirectionMovesPieceByPiece(isBlue,  true));
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue, true));
        System.out.println();
        System.out.println("directionByDirectionMovesPieceByPiece from back to front");
        System.out.println(directionByDirectionMovesPieceByPiece(isBlue, false));
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue, false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("allTypeMovesDirectionByDirection from front to back");
        System.out.println(allTypeMovesDirectionByDirection(isBlue,  true));
        allStyles.add(allTypeMovesDirectionByDirection(isBlue, true));
        System.out.println();
        System.out.println("allTypeMovesDirectionByDirection from back to front");
        System.out.println(allTypeMovesDirectionByDirection(isBlue, false));
        allStyles.add(allTypeMovesDirectionByDirection(isBlue, true));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("typeByTypeMovesDirectionByDirection from front to back");
        System.out.println(typeByTypeMovesDirectionByDirection(isBlue, true));
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue, true));
        System.out.println();
        System.out.println("typeByTypeMovesDirectionByDirection from back to front");
        System.out.println(typeByTypeMovesDirectionByDirection(isBlue, false));
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("directionByDirectionMovesTypeByType from front to back");
        allStyles.add(directionByDirectionMovesTypeByType(isBlue,true));
        System.out.println();
        System.out.println("directionByDirectionMovesTypeByType from back to front");
        System.out.println(directionByDirectionMovesTypeByType(isBlue, false));
        allStyles.add(directionByDirectionMovesTypeByType(isBlue, false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        return allStyles;
    }

}
