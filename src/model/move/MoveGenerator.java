package model.move;
import model.Board;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private Board board;
    private MoveGeneratingStyle style;
    MoveType[] moveTypes;
    int[] directions;

    public MoveGenerator(Board board, MoveGeneratingStyle style, MoveType[] moveTypes, int[] directions) {
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
        //if ();
        return null;
    }

    public List<Move> allTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        return board.allTypeMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> typeByTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        return board.typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions, frontToBack);
    }

    public List<Move> directionByDirectionMovesPieceByPiece(boolean isBlue, int[] directions, boolean frontToBack){
        return board.directionByDirectionMovesPieceByPiece(isBlue, directions, frontToBack);
    }

    public List<Move> allTypeMovesDirectionByDirection (boolean isBlue, int[] directions, boolean frontToBack){
        return board.allTypeMovesDirectionByDirection(isBlue, directions, frontToBack);
    }
    public List<Move> typeByTypeMovesDirectionByDirection(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        return board.typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions, frontToBack);
    }
    public List<Move> directionByDirectionMovesTypeByType(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack){
        return board.directionByDirectionMovesTypeByType(isBlue, moveTypes, directions, frontToBack);
    }

    public List<List<Move>> generateAllStyles (boolean isBlue, MoveType[] moveTypes, int[] directions){
        List<List<Move>> allStyles = new ArrayList<>();

        System.out.println("allTypeMovesPieceByPiece from front to back");
        System.out.println(allTypeMovesPieceByPiece(isBlue, moveTypes, directions,true));
        allStyles.add(allTypeMovesPieceByPiece(isBlue, moveTypes, directions,true));
        System.out.println();
        System.out.println("allTypeMovesPieceByPiece from back to front");
        System.out.println(allTypeMovesPieceByPiece(isBlue, moveTypes, directions,false));
        allStyles.add(allTypeMovesPieceByPiece(isBlue, moveTypes, directions,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("typeByTypeMovesPieceByPiece from front to back");
        System.out.println(typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions,true));
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions,true));
        System.out.println();
        System.out.println("typeByTypeMovesPieceByPiece from back to front");
        System.out.println(typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions,false));
        allStyles.add(typeByTypeMovesPieceByPiece(isBlue, moveTypes, directions,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("directionByDirectionMovesPieceByPiece from front to back");
        System.out.println(directionByDirectionMovesPieceByPiece(isBlue, directions,true));
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue, directions,true));
        System.out.println();
        System.out.println("directionByDirectionMovesPieceByPiece from back to front");
        System.out.println(directionByDirectionMovesPieceByPiece(isBlue, directions,false));
        allStyles.add(directionByDirectionMovesPieceByPiece(isBlue, directions,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("allTypeMovesDirectionByDirection from front to back");
        System.out.println(allTypeMovesDirectionByDirection(isBlue, directions,true));
        allStyles.add(allTypeMovesDirectionByDirection(isBlue, directions,true));
        System.out.println();
        System.out.println("allTypeMovesDirectionByDirection from back to front");
        System.out.println(allTypeMovesDirectionByDirection(isBlue, directions,false));
        allStyles.add(allTypeMovesDirectionByDirection(isBlue, directions,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("typeByTypeMovesDirectionByDirection from front to back");
        System.out.println(typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions,true));
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions,true));
        System.out.println();
        System.out.println("typeByTypeMovesDirectionByDirection from back to front");
        System.out.println(typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions,false));
        allStyles.add(typeByTypeMovesDirectionByDirection(isBlue, moveTypes, directions,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("directionByDirectionMovesTypeByType from front to back");
        System.out.println(directionByDirectionMovesTypeByType(isBlue, moveTypes, directions,true));
        allStyles.add(directionByDirectionMovesTypeByType(isBlue, moveTypes, directions,true));
        System.out.println();
        System.out.println("directionByDirectionMovesTypeByType from back to front");
        System.out.println(directionByDirectionMovesTypeByType(isBlue, moveTypes, directions,false));
        allStyles.add(directionByDirectionMovesTypeByType(isBlue, moveTypes, directions,false));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        return allStyles;
    }

}
