package model.bit;

import model.Board;
import model.move.Move;
import model.move.MoveType;

import java.util.List;

public class BitBoard extends Board {
    private final long row_1 = 255L;
    private final long row_2 = 65280L;
    private final long row_3 = 16711680L;
    private final long row_4 = 4278190080L;
    private final long row_5 = 1095216660480L;
    private final long row_6 = 280375465082880L;
    private final long row_7 = 71776119061217280L;
    private final long col_A = 36170086419038336L;
    private final long col_B = 18085043209519168L;
    private final long col_H = 282578800148737L;
    private final long col_G = 565157600297474L;

    private long bw;
    private long bt;
    private long rw;
    private long rt;
    private String FEN;

    @Override
    public void build(String FEN) {
        cleanBoard();
        int squareCol;
        int squareLoc;
        int bitLoc;
        long mask;
        String [] rows = FEN.split("[/\\s]+");
        for (int r = 0; r < 7; r++){
            squareCol = 0;
            for (int c = 0; c < rows[r].length(); c++){
                if (Character.isDigit(rows[r].charAt(c))){
                    squareCol += Character.getNumericValue(rows[r].charAt(c));
                }
                else {
                    squareCol++;
                    squareLoc = (r * 8) + (squareCol - 1);
                    bitLoc = 55 - squareLoc;
                    mask = 1L << bitLoc;
                    if (rows[r].charAt(c) == 'w') rw |= mask;
                    else if (rows[r].charAt(c) == 'W') bw |= mask;
                    else if (rows[r].charAt(c) == 't') rt |= mask;
                    else bt |= mask;
                }
            }
        }
    }

    @Override

    public String generateFEN() {
        char[] board = String.format("%064d", Long.parseLong(Long.toBinaryString(rw))).replace('1','w').toCharArray();
        long blueWalls = bw;
        long redTowers = rt;
        long blueTowers = bt;
        int index;

        while (blueWalls != 0){
            index = Long.numberOfLeadingZeros(blueWalls);
            blueWalls ^= Long.highestOneBit(blueWalls);
            board[index] ='W';
        }
        while (redTowers != 0){
            index = Long.numberOfLeadingZeros(redTowers);
            redTowers ^= Long.highestOneBit(redTowers);
            board[index] = 't';
        }
        while (blueTowers != 0){
            index = Long.numberOfLeadingZeros(blueTowers);
            blueTowers ^= Long.highestOneBit(blueTowers);
            board[index] = 'T';
        }

        StringBuilder fen = new StringBuilder();
        for (int row = 1; row < 8; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 8; col++) {
                char piece = board[row * 8 + col];
                if (piece == '0')emptyCount++;
                else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece);
                }
            }
            if (emptyCount > 0) fen.append(emptyCount);
            fen.append('/');
        }
        return fen.toString();
    }

        @Override
    public void makeMove(Move move, boolean isBlue) {

    }

    @Override
    public void unmakeMove(Move move, boolean isBlue) {

    }

    @Override
    public void cleanBoard() {
        rt = rw = bt = bw = 0L;
    }

    @Override
    public List<Move> generateSacrificingMoves(boolean isBlue) {
        return null;
    }

    @Override
    public List<Move> generateQuietMoves(boolean isBlue) {
        return null;
    }

    @Override
    public List<Move> generateMixedMoves(boolean isBlue) {
        return null;
    }

    @Override
    public int towersDistances(boolean isBlue, int[] values) {
        long pieces = isBlue ? bt : rt;
        return distancesFinder(isBlue,pieces,values);
    }

    @Override
    public int wallsDistances(boolean isBlue, int[] values) {
        long pieces = isBlue ? bw : rw;
        return distancesFinder(isBlue,pieces,values);
    }


    @Override
    public int towersColumns(boolean isBlue, int[] values) {
        long pieces = isBlue ? bt : rt;
        return columnsFinder(pieces, values);
    }

    @Override
    public int wallsColumns(boolean isBlue, int[] values) {
        long pieces = isBlue ? bw : rw;
        return columnsFinder(pieces, values);
    }

    @Override
    public int wallsNumber(boolean isBlue) {
        return isBlue ? Long.bitCount(bw) :  Long.bitCount(rw);
    }

    @Override
    public int towersNumber(boolean isBlue) {
        return isBlue ? Long.bitCount(bt) :  Long.bitCount(rt);
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
    public boolean lostGame(boolean isBlue) {
        return false;
    }

    @Override
    public boolean isFriendlyTower(boolean isBlue, int location) {
        return false;
    }

    @Override
    public boolean isFriendlyPiece(boolean isBlue, int location) {
        return false;
    }

    @Override
    public List<Short> normalMovesLocations(boolean isBlue, int location, int startDirection, boolean clockwise) {
        return null;
    }

    @Override
    public List<Short> sacrificingMovesLocations(boolean isBlue, int location, int startDirection, boolean clockwise) {
        return null;
    }

    @Override
    public List<Move> allTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        return null;
    }

    @Override
    public List<Move> typeByTypeMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        return null;
    }

    @Override
    public List<Move> directionByDirectionMovesPieceByPiece(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        return null;
    }

    @Override
    public List<Move> allTypeMovesDirectionByDirection(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        return null;
    }

    @Override
    public List<Move> typeByTypeMovesDirectionByDirection(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        return null;
    }

    @Override
    public List<Move> directionByDirectionMovesTypeByType(boolean isBlue, MoveType[] moveTypes, int[] directions, boolean frontToBack) {
        return null;
    }

    ///////////////////////////////////////////////////////////////////////

    public int distancesFinder(boolean isBlue, long locations, int[] values) {
        long index = row_1;
        int row, result = 0;
        for (int i = 0; i < 7 ; i++){
            row = isBlue ? i : 6 - i;
            result += values[row] * (Long.bitCount(locations & index));
            index = index << 8;
        }
        return result;
    }

    public int columnsFinder(long locations, int[] values) {
        long indexLeft = col_A;
        long indexRight = col_H;
        int result = 0;
        for (int i = 0; i < 4 ; i++){
            result += values[i] * (Long.bitCount(locations & (indexRight|indexLeft)));
            indexRight <<= 8;
            indexLeft >>= 8;
        }
        return result;
    }
}
