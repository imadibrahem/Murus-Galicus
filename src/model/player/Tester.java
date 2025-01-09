package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveComparator;
import model.move.MoveGenerator;
import model.transpositionTable.TranspositionTable;
import view.DisplayBoard;
import view.DisplayFrame;

import java.awt.*;
import java.util.List;
import java.util.Scanner;

public class Tester extends Player{
    String FEN = board.generateFEN();
    DisplayFrame displayFrame;
    DisplayBoard displayBoard;
    short oldMoveInitial = 0;
    short oldMoveFirst = 0;
    short oldMoveSecond = 0;
    private final int searchDepth;
    private int currentSearchDepth = 1;
    private Move best;
    private Move globalBest;
    private final int window;
    private final int windowMultiplier;
    private final Move[][][] killerMoves = new Move[MAX_DEPTH][3][2];
    private final int[][] maxHistoryTable = new int[56][8];
    private final int[][] minHistoryTable = new int[56][8];
    private final MoveComparator maxComparator;
    private final MoveComparator minComparator;
    private final int interactiveDepthRatio;
    private final int reduction;
    private String nullMoveFen = "";
    private final int fullDepthMoveNumber;
    private final float roundsFactor;
    private final float towersFactor;
    private final float distancesFactor;
    private final TranspositionTable transpositionTable;
    int secondScore = 0;
    String aspiration = "";
    int counter = 0;
    Scanner scanner = new Scanner(System.in);

    public Tester(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction,
                       int window, int windowMultiplier, int interactiveDepthRatio, int searchDepth, int reduction, int fullDepthMoveNumber,
                       float roundsFactor, float towersFactor, float distancesFactor) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        this.window = window;
        this.windowMultiplier = windowMultiplier;
        this.interactiveDepthRatio = interactiveDepthRatio;
        this.reduction = reduction;
        this.fullDepthMoveNumber = fullDepthMoveNumber;
        this.roundsFactor = roundsFactor;
        this.towersFactor = towersFactor;
        this.distancesFactor = distancesFactor;
        maxComparator = new MoveComparator(isEvaluationBlue(),board, maxHistoryTable, killerMoves);
        minComparator = new MoveComparator(!isEvaluationBlue(),board, minHistoryTable, killerMoves);
        transpositionTable = new TranspositionTable();
        displayFrame = new DisplayFrame(FEN);
        displayBoard = displayFrame.getDisplayBoard();
        System.out.println("Table created..");
    }

    public Tester(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        this.window = 10;
        this.windowMultiplier = 3;
        this.interactiveDepthRatio = 4;
        this.reduction = 3;
        this.fullDepthMoveNumber = 7;
        this.roundsFactor = 0.5f;
        this.towersFactor = -0.15f;
        this.distancesFactor = -1f;
        maxComparator = new MoveComparator(isEvaluationBlue(),board, maxHistoryTable, killerMoves);
        minComparator = new MoveComparator(!isEvaluationBlue(),board, minHistoryTable, killerMoves);
        transpositionTable= new TranspositionTable();
        displayFrame = new DisplayFrame(FEN);
        displayBoard = displayFrame.getDisplayBoard();
        System.out.println("Table created..");
    }


    @Override
    public Move decideMove() {
        counter = 0;
        System.out.println("Starting move decision process...");
        zobristHashing.computeHash();
        System.out.println("Hashing = " + zobristHashing.getHash());
        globalBest = null;
        int aspirationScore = evaluationFunction.evaluate(isEvaluationBlue, 0);
        System.out.println("Initial aspiration score: " + aspirationScore);
        while (currentSearchDepth < (searchDepth + 1)){
            System.out.println("Starting depth: " + currentSearchDepth);
            iterateDepth(currentSearchDepth, aspirationScore);
            currentSearchDepth++;
        }
        movesNodes.add(moveNodes);
        nodes += moveNodes;
        moveNodes = 0;
        currentSearchDepth = 1;
        System.out.println("Decision process complete. Best move: " + globalBest);
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return globalBest;
    }

    private int iterateDepth(int depth, int score) {
        System.out.println("00000000000000000000000000000000000000000000000000000000");
        System.out.println("Iterating depth " + depth + " with score: " + score);
        int aspirationScore = aspirationWindowsSearch(depth, score, window, windowMultiplier);
        globalBest = best;
        System.out.println("Depth " + depth + " iteration complete. Aspiration score: " + aspirationScore);
        System.out.println("00000000000000000000000000000000000000000000000000000000");
        return aspirationScore;
    }

    public int aspirationWindowsSearch(int depth, int score, int window, int windowMultiplier){
        int alpha = score - window;
        int beta = score + window;
        aspiration = "Starting aspiration window search with depth: " + depth + ", alpha: " + alpha + ", beta: " + beta;
        if (rounds == 2) secondScore = score;
        System.out.println(aspiration);
        while (true) {
            score = maximizer(depth, alpha, beta);
            System.out.println("Aspiration search score: " + score + ", alpha: " + alpha + ", beta: " + beta);
            if (score <= alpha){
                System.out.println("Aspiration Window is small for alpha: alpha = " + alpha + " , score = " + score);
                window =(windowMultiplier * window);
                alpha -= window;
                System.out.println("Score <= alpha. New alpha: " + alpha);
            }
            else if (score >= beta) {
                System.out.println("Aspiration Window is small for beta: beta = " + beta + " , score = " + score);
                window =(windowMultiplier * window);
                beta += window ;
                System.out.println("Score >= beta. New beta: " + beta);
            }
            else{
                System.out.println("Aspiration search completed with score: " + score);
                return score;
            }
        }
    }

    private int maximizer(int depth, int alpha, int beta) {
        counter++;
        //if (counter > 30)return 0;
        System.out.println("Maximizer called with depth: " + depth + ", alpha: " + alpha + ", beta: " + beta);
        TranspositionTable.TranspositionEntry entry = transpositionTable.get(zobristHashing.getHash());
        if (entry != null && entry.getDepth() >= depth) {
            System.out.println("Maximizer: Transposition table entry found for hash: " + zobristHashing.getHash());
            if (entry.getFlag() == TranspositionTable.TranspositionEntry.EXACT){
                System.out.println("Maximizer: Exact entry found with score: " + entry.getScore());
                if (!doNull){
                    System.out.println("sfsdffe");
                    board.build(nullMoveFen);
                    doNull = true;
                }
                return entry.getScore();
            }
            else if (entry.getFlag() == TranspositionTable.TranspositionEntry.LOWERBOUND) {
                System.out.println("Maximizer: Lowerbound entry updated alpha to: " + alpha);
                alpha = Math.max(alpha, entry.getScore());
            }
            else if (entry.getFlag() == TranspositionTable.TranspositionEntry.UPPERBOUND){
                System.out.println("Maximizer: Upperbound entry updated beta to: " + beta);
                beta = Math.min(beta, entry.getScore());
            }
            if (alpha >= beta){
                System.out.println("Maximizer: Pruning with score from transposition table: " + entry.getScore());
                if (!doNull){
                    System.out.println("sfsdffe");
                    board.build(nullMoveFen);
                    doNull = true;
                }
                return entry.getScore();
            }
        }
        if (board.lostGame(true) || board.lostGame(false)){
            doNull = true;
            int score = evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
            transpositionTable.put(zobristHashing.getHash(),depth, score, entryPriority(depth), TranspositionTable.TranspositionEntry.EXACT, new Move((short) 0));
            //transpositionTable.put(zobristHashing.getHash(),depth, score, (rounds + depth), TranspositionEntry.EXACT, new Move((short) 0));
            System.out.println("Maximizer: One Player lost and entry added with score: " + score );
            return score;
        }
        if (depth == 0){
            System.out.println("Maximizer: Searched all depths and starting with Quiescence Search");
            doNull = true;
            int q = quiescenceSearch(currentSearchDepth, alpha, beta);
            transpositionTable.put(zobristHashing.getHash(),depth, q, entryPriority(depth), TranspositionTable.TranspositionEntry.EXACT, new Move((short) 0));
            //transpositionTable.put(zobristHashing.getHash(),depth, q, (rounds + depth), TranspositionEntry.EXACT, new Move((short) 0));
            System.out.println("Maximizer: Quiescence Search finished and entry added with score: " + q );
            return q;
        }
        moveNodes++;
        List<Move> allMoves = maxComparator.filterAndSortMoves( moveGenerator.generateMoves(isEvaluationBlue), globalBest, depth, depth == currentSearchDepth, board.isInLosingPos(isEvaluationBlue), board.isInCheck(isEvaluationBlue));
        System.out.println("Maximizer: all Moves Length: " + allMoves.size());
        System.out.println("Maximizer: All Moves Generated");
        if (entry != null && entry.getBestMoveValue() != 0){
            Move move = new Move(entry.getBestMoveValue());
            if (!move.equals(allMoves.get(0))){
                System.out.println("-----------------------");
                System.out.println("-----------------------");
                System.out.println("-----------------------");
                System.out.println(allMoves);
                System.out.println(move);
                allMoves.remove(move);
                allMoves.add(0, move);
                System.out.println("Maximizer: New Best Move from Transpositions table found: " + move);
                System.out.println("-----------------------");
                System.out.println("-----------------------");
                System.out.println("-----------------------");
            }
        }
        boolean firstMove = true;
        doNull = true;
        int moveIndex = 0;
        int initialAlpha = alpha;
        for (Move move : allMoves) {
            zobristHashing.updateHashForMoves(move,isEvaluationBlue);
            moveIndex++;
            System.out.println("Maximizer: Move index: " + moveIndex);
            /*
            if (doNull && !firstMove && depth > (reduction + 1) && (!board.isInCheck(isEvaluationBlue)) && board.towersNumber(isEvaluationBlue) > 2){
                System.out.println("Maximizer: Performing null move pruning at depth " + depth);
                doNull = false;
                nullMoveFen = board.generateFEN();
                switchColor();
                int nullMoveScore  = minimizer(depth - 1 - reduction, beta - 1, beta);
                switchColor();
                board.build(nullMoveFen);
                zobristHashing.computeHash();
                if (nullMoveScore >= beta) {
                    doNull = true;
                    return beta;
                }
            }

             */
            System.out.println("Maximizer: Making Move: " + move);
            makeMove(move);
            display(move);
            switchColor();
            int rating;
            if (firstMove) {
                rating = minimizer(depth - 1, alpha, beta);
                firstMove = false;
            }
            else {
                int reducedDepth = depth - 1;
                //if (depth > 3 && moveIndex > fullDepthMoveNumber && board.towersNumber(isEvaluationBlue) > 2 && board.towersNumber(isEvaluationBlue) > 2
                  //      && (!board.isInCheck(isEvaluationBlue))  && (!board.isInCheck(!isEvaluationBlue))) reducedDepth = depth - 2;
                rating = minimizer( reducedDepth, alpha, alpha + 1);
                if (rating > alpha && rating < beta) {
                    rating = minimizer(depth - 1, alpha, beta);
                }
            }
            zobristHashing.updateHashForMoves(move,isEvaluationBlue);
            System.out.println("Maximizer: Unmaking Move: " + move);
            switchColor();
            unmakeMove(move);
            display(move);
            if (rating > alpha){
                alpha = rating;
                if (depth == currentSearchDepth) best = move;
                updateHistoryTable(move, depth, true, isBlue());
                if (alpha >= beta){
                    int type = 1;
                    if (move.isTargetEmpty()) type = 0;
                    else if (move.isTargetEnemy()) type = 2;
                    storeKillerMove(move, depth, type);
                    doNull = true;
                    transpositionTable.put(zobristHashing.getHash(),depth, alpha, entryPriority(depth), TranspositionTable.TranspositionEntry.LOWERBOUND, move);
                    //transpositionTable.put(zobristHashing.getHash(),depth, alpha, (rounds + depth), TranspositionEntry.LOWERBOUND, move);
                    System.out.println("Maximizer: LOWERBOUND Beta cutoff entry found with score: " + alpha + " and move: " + move);
                    return alpha;
                }
                transpositionTable.put(zobristHashing.getHash(),depth, alpha, entryPriority(depth), TranspositionTable.TranspositionEntry.EXACT, move);
                //transpositionTable.put(zobristHashing.getHash(),depth, alpha, (rounds + depth), TranspositionEntry.EXACT, move);
                System.out.println("Maximizer: Exact entry found with score: " + alpha + " and move: " + move);
            }

        }
        doNull = true;
        if (alpha == initialAlpha) {
            transpositionTable.put(zobristHashing.getHash(), depth, alpha, entryPriority(depth), TranspositionTable.TranspositionEntry.UPPERBOUND, best);
            System.out.println("Maximizer: UPPERBOUND entry found with score: " + alpha + " and move: " + best);

        }
        //transpositionTable.put(zobristHashing.getHash(),depth, alpha, (rounds + depth), TranspositionEntry.UPPERBOUND, best);
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        System.out.println("Minimizer called with depth: " + depth + ", alpha: " + alpha + ", beta: " + beta);
        TranspositionTable.TranspositionEntry entry = transpositionTable.get(zobristHashing.getHash());
        if (entry != null && entry.getDepth() >= depth) {
            System.out.println("Minimizer: Transposition table entry found for hash: " + zobristHashing.getHash());
            if (entry.getFlag() == TranspositionTable.TranspositionEntry.EXACT){
                System.out.println("Minimizer: Exact entry found with score: " + entry.getScore());
                if (!doNull){
                    System.out.println("sfsdffe");
                    board.build(nullMoveFen);
                    doNull = true;
                }
                return entry.getScore();
            }
            else if (entry.getFlag() == TranspositionTable.TranspositionEntry.LOWERBOUND){
                System.out.println("Minimizer: Lowerbound entry updated alpha to: " + alpha);
                alpha = Math.max(alpha, entry.getScore());
            }
            else if (entry.getFlag() == TranspositionTable.TranspositionEntry.UPPERBOUND){
                System.out.println("Minimizer: Upperbound entry updated beta to: " + beta);
                beta = Math.min(beta, entry.getScore());
            }
            if (alpha >= beta) {
                System.out.println("Minimizer: Pruning with score from transposition table: " + entry.getScore());
                if (!doNull){
                    System.out.println("sfsdffe");
                    board.build(nullMoveFen);
                    doNull = true;
                }
                return entry.getScore();
            }
        }
        if (board.lostGame(true) || board.lostGame(false)){
            doNull = true;
            int score = evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
            transpositionTable.put(zobristHashing.getHash(),depth, score, entryPriority(depth), TranspositionTable.TranspositionEntry.EXACT, new Move((short) 0));
            System.out.println("Minimizer: One Player lost and entry added with score: " + score );
            //transpositionTable.put(zobristHashing.getHash(),depth, score, (rounds + depth), TranspositionEntry.EXACT, new Move((short) 0));
            return score;
        }
        if (depth == 0){
            doNull = true;
            System.out.println("Minimizer: Searched all depths and starting with Quiescence Search");
            int q = quiescenceSearch(currentSearchDepth, alpha, beta);
            transpositionTable.put(zobristHashing.getHash(),depth, q, entryPriority(depth), TranspositionTable.TranspositionEntry.EXACT, new Move((short) 0));
            System.out.println("Minimizer: Quiescence Search finished and entry added with score: " + q );
            //transpositionTable.put(zobristHashing.getHash(),depth, q, (rounds + depth), TranspositionEntry.EXACT, new Move((short) 0));
            return q;
        }
        moveNodes++;
        List<Move> allMoves = minComparator.filterAndSortMoves( moveGenerator.generateMoves(!isEvaluationBlue), globalBest, depth,false ,board.isInLosingPos(!isEvaluationBlue), board.isInCheck(!isEvaluationBlue));
        System.out.println("Minimizer: all Moves Length: " + allMoves.size());
        System.out.println("Minimizer: All Moves Generated");
        if (entry != null && entry.getBestMoveValue() != 0){
            Move move = new Move(entry.getBestMoveValue());
            if (!move.equals(allMoves.get(0))){
                System.out.println(allMoves);
                System.out.println(move);
                allMoves.remove(move);
                allMoves.add(0, move);
                System.out.println("Minimizer: New Best Move from Transpositions table found: " + move);
            }
        }
        boolean firstMove = true;
        doNull = true;
        int moveIndex = 0;
        int initialBeta = beta;
        for (Move move : allMoves) {
            zobristHashing.updateHashForMoves(move,!isEvaluationBlue);
            moveIndex++;
            System.out.println("Minimizer: Move index: "+ moveIndex);
            /*
            if (doNull && !firstMove && depth > (reduction + 1) && (!board.isInCheck(!isEvaluationBlue)) && board.towersNumber(!isEvaluationBlue) > 2){
                System.out.println("Minimizer: Performing null move pruning at depth " + depth);
                doNull = false;
                nullMoveFen = board.generateFEN();
                switchColor();
                int nullMoveScore  = maximizer(depth - 1 - reduction, alpha, alpha + 1);
                switchColor();
                board.build(nullMoveFen);
                zobristHashing.computeHash();
                if (nullMoveScore <= alpha) {
                    System.out.println("Minimizer: Null move entry found with score:" + alpha);
                    doNull = true;
                    return alpha;
                }
            }

             */
            System.out.println("Minimizer: Making Move: " + move);
            makeMove(move);
            display(move);
            switchColor();
            int rating;
            if (firstMove) {
                rating = maximizer(depth - 1, alpha, beta);
                firstMove = false;
            } else {
                int reducedDepth = depth - 1;
                //if (depth > 3 && moveIndex > fullDepthMoveNumber && board.towersNumber(isEvaluationBlue) > 2 && board.towersNumber(isEvaluationBlue) > 2
                  //      && (!board.isInCheck(isEvaluationBlue))  && (!board.isInCheck(!isEvaluationBlue))) reducedDepth = depth - 2;
                rating = maximizer(reducedDepth, beta - 1, beta);
                if (rating > alpha && rating < beta) {
                    rating = maximizer(depth - 1, alpha, beta);
                }
            }
            zobristHashing.updateHashForMoves(move,!isEvaluationBlue);
            System.out.println("Minimizer: Unmaking Move: " + move);
            switchColor();
            unmakeMove(move);
            display(move);
            if (rating < beta) {
                beta = rating;
                updateHistoryTable(move, depth, false, isBlue());
                if (alpha >= beta) {
                    int type = 1;
                    if (move.isTargetEmpty()) type = 0;
                    else if (move.isTargetEnemy()) type = 2;
                    storeKillerMove(move, depth, type);
                    doNull = true;
                    transpositionTable.put(zobristHashing.getHash(),depth, beta, entryPriority(depth), TranspositionTable.TranspositionEntry.UPPERBOUND, move);
                    //transpositionTable.put(zobristHashing.getHash(),depth, beta, (rounds + depth), TranspositionEntry.UPPERBOUND, move);
                    System.out.println("Minimizer: UPPERBOUND  Beta cutoff entry found with score: " + beta + " and move: " + move);
                    return beta;
                }
                transpositionTable.put(zobristHashing.getHash(),depth, beta, entryPriority(depth), TranspositionTable.TranspositionEntry.EXACT, move);
                //transpositionTable.put(zobristHashing.getHash(), depth, beta, (rounds + depth), TranspositionEntry.EXACT, move);
                System.out.println("Minimizer: Exact entry found with score: " + alpha + " and move: " + move);

            }
        }
        doNull = true;
        if (beta == initialBeta){
            transpositionTable.put(zobristHashing.getHash(), depth, beta, entryPriority(depth), TranspositionTable.TranspositionEntry.LOWERBOUND, best);
            System.out.println("Minimizer: UPPERBOUND entry found with score: " + alpha + " and move: " + best);
        }
        //transpositionTable.put(zobristHashing.getHash(),beta, alpha, entryPriority(depth), TranspositionEntry.LOWERBOUND, best);
        return beta;
    }

    private int quiescenceSearch(int depth, int alpha, int beta) {
        System.out.println("Quiescence search called with depth: " + depth + ", alpha: " + alpha + ", beta: " + beta);
        if (board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue,depth);
        moveNodes++;
        int standPat = evaluationFunction.evaluate(isEvaluationBlue, depth);
        boolean isMaximizingPlayer = (isBlue() == isEvaluationBlue);
        if (isMaximizingPlayer) {
            if (standPat >= beta) return beta;
            if (alpha < standPat) alpha = standPat;
        } else {
            if (standPat <= alpha) return alpha;
            if (beta > standPat) beta = standPat;
        }
        List<Move> loudMoves;
        if ((depth - currentSearchDepth) <= (currentSearchDepth / interactiveDepthRatio)) loudMoves = moveGenerator.generateThreateningMoves(isBlue());
        else loudMoves = moveGenerator.generateLoudMoves(isBlue());
        if (isMaximizingPlayer) loudMoves = maxComparator.quiescenceFilterAndSortMoves( loudMoves, board.isInLosingPos(isEvaluationBlue), board.isInCheck(isEvaluationBlue));
        else loudMoves = minComparator.quiescenceFilterAndSortMoves( loudMoves,  board.isInLosingPos(!isEvaluationBlue), board.isInCheck(!isEvaluationBlue));
        for (Move move : loudMoves) {
            makeMove(move);
            displayMove(move,200);
            switchColor();
            int score = quiescenceSearch(depth + 1 ,alpha, beta);
            switchColor();
            unmakeMove(move);
            displayMove(move,200);
            if (isMaximizingPlayer) {
                if (score >= beta) return beta;
                if (score > alpha) alpha = score;
            } else {
                if (score <= alpha) return alpha;
                if (score < beta) beta = score;
            }
        }
        return isMaximizingPlayer ? alpha : beta;
    }

    private void updateHistoryTable(Move move, int depth, boolean isMax, boolean isBlue) {
        if (isMax) maxHistoryTable [move.getInitialLocation(isBlue)][move.getDirection() - 1] += depth * depth;
        else minHistoryTable [move.getInitialLocation(isBlue)][move.getDirection() - 1] += depth * depth;
    }

    private void storeKillerMove(Move move, int depth, int type) {
        if (killerMoves[depth][type][0] == null) killerMoves[depth][type][0] = move;
        else if (!killerMoves[depth][type][0].equals(move) && killerMoves[depth][type][1] == null) killerMoves[depth][type][1] = move;
        else if (!killerMoves[depth][type][0].equals(move) && !killerMoves[depth][type][1].equals(move)){
            killerMoves[depth][type][0] = killerMoves[depth][type][1];
            killerMoves[depth][type][1] = move;
        }
    }

    private int entryPriority(int depth){
        return priority(depth, roundsFactor, towersFactor, distancesFactor);
    }

    @Override public String tableUsageReport(){
        double percentage = (transpositionTable.used /(double)transpositionTable.getTableSize()) * 100;
        return "Entries used = " + transpositionTable.used + " | Total Entries Number = " + transpositionTable.getTableSize() + " | Percentage = " + percentage + " %";
    }



    public void colorOldMove(boolean isBlue, Move move){
        displayBoard.displaySquare[oldMoveInitial].returnOldColor();
        displayBoard.displaySquare[oldMoveFirst].returnOldColor();
        displayBoard.displaySquare[oldMoveSecond].returnOldColor();
        int initial = move.getInitialLocation(isBlue);
        oldMoveInitial = (short) initial;
        if (move.isTargetEnemy()){
            oldMoveFirst = board.sacrificingMovesLocation(isBlue, initial,move.getDirection());
            displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        }
        else {
            short[] targets = board.normalMovesLocation(isBlue, initial,move.getDirection());
            oldMoveFirst = targets[0];
            oldMoveSecond = targets[1];
            displayBoard.displaySquare[oldMoveSecond].changeColor(Color.orange);
        }
        displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        displayBoard.displaySquare[oldMoveInitial].changeColor(Color.BLUE);

    }

    public void displayMove(Move move, long timer){
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FEN = board.generateFEN();
        colorOldMove(isBlue(), move);
        displayBoard.updateBoard(FEN);
    }

    public void display(Move move){
        scanner.nextLine();
        FEN = board.generateFEN();
        colorOldMove(isBlue(), move);
        displayBoard.updateBoard(FEN);
    }
}
