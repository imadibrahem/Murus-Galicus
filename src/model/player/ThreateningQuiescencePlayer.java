package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveComparator;
import model.move.MoveGenerator;

import java.util.List;

public class ThreateningQuiescencePlayer extends Player {
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

    public ThreateningQuiescencePlayer(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int window, int windowMultiplier, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        this.window = window;
        this.windowMultiplier = windowMultiplier;
        maxComparator = new MoveComparator(isEvaluationBlue(),board, maxHistoryTable, killerMoves);
        minComparator = new MoveComparator(!isEvaluationBlue(),board, minHistoryTable, killerMoves);

    }

    public ThreateningQuiescencePlayer(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        this.window = 10;
        this.windowMultiplier = 3;
        maxComparator = new MoveComparator(isEvaluationBlue(),board, maxHistoryTable, killerMoves);
        minComparator = new MoveComparator(!isEvaluationBlue(),board, minHistoryTable, killerMoves);
    }


    @Override
    public Move decideMove() {
        globalBest = null;
        int aspirationScore = evaluationFunction.evaluate(isEvaluationBlue, 0);
        while (currentSearchDepth < (searchDepth + 1)){
            iterateDepth(currentSearchDepth, aspirationScore);
            currentSearchDepth++;
        }
        movesNodes.add(moveNodes);
        nodes += moveNodes;
        moveNodes = 0;
        currentSearchDepth = 1;
        return globalBest;
    }

    private int iterateDepth(int depth, int score) {
        int aspirationScore = aspirationWindowsSearch(depth, score, window, windowMultiplier);
        globalBest = best;
        return aspirationScore;
    }

    public int aspirationWindowsSearch(int depth, int score, int window, int windowMultiplier){
        int alpha = score - window;
        int beta = score + window;
        while (true) {
            score = maximizer(depth, alpha, beta);
            if (score <= alpha) alpha -= (windowMultiplier * window);
            else if (score >= beta) beta += (windowMultiplier * window);
            else return score;
        }
    }

    private int maximizer(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        if (depth == 0) return quiescenceSearch(currentSearchDepth, alpha, beta);
        moveNodes++;
        List<Move> allMoves = maxComparator.filterAndSortMoves( moveGenerator.generateMoves(isEvaluationBlue), globalBest, depth, depth == currentSearchDepth, board.isInLosingPos(isEvaluationBlue), board.isInCheck(isEvaluationBlue));
        boolean firstMove = true;
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int rating;
            if (firstMove) {
                rating = minimizer(depth - 1, alpha, beta);
                firstMove = false;
            } else {
                rating = minimizer(depth - 1, alpha, alpha + 1);
                if (rating > alpha && rating < beta) {
                    rating = minimizer(depth - 1, alpha, beta);
                }
            }
            switchColor();
            unmakeMove(move);
            if (rating > alpha){
                alpha = rating;
                if (depth == currentSearchDepth) best = move;
                updateHistoryTable(move, depth, true, isBlue());
            }
            if (alpha >= beta){
                int type = 1;
                if (move.isTargetEmpty()) type = 0;
                else if (move.isTargetEnemy()) type = 2;
                storeKillerMove(move, depth, type);
                return alpha;
            }
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        if (depth == 0) return quiescenceSearch(currentSearchDepth, alpha, beta);
        moveNodes++;
        List<Move> allMoves = minComparator.filterAndSortMoves( moveGenerator.generateMoves(!isEvaluationBlue), globalBest, depth,false ,board.isInLosingPos(!isEvaluationBlue), board.isInCheck(!isEvaluationBlue));
        boolean firstMove = true;
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int rating;
            if (firstMove) {
                rating = maximizer(depth - 1, alpha, beta);
                firstMove = false;
            } else {
                rating = maximizer(depth - 1, beta - 1, beta);
                if (rating > alpha && rating < beta) {
                    rating = maximizer(depth - 1, alpha, beta);
                }
            }
            switchColor();
            unmakeMove(move);
            if (rating < beta) {
                beta = rating;
                updateHistoryTable(move, depth, false, isBlue());
            }
            if (alpha >= beta) {
                int type = 1;
                if (move.isTargetEmpty()) type = 0;
                else if (move.isTargetEnemy()) type = 2;
                storeKillerMove(move, depth, type);
                return beta;
            }
        }
        return beta;
    }

    private int quiescenceSearch(int depth, int alpha, int beta) {
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
        loudMoves = moveGenerator.generateThreateningMoves(isBlue());
        if (isMaximizingPlayer) loudMoves = maxComparator.quiescenceFilterAndSortMoves( loudMoves, board.isInLosingPos(isEvaluationBlue), board.isInCheck(isEvaluationBlue));
        else loudMoves = minComparator.quiescenceFilterAndSortMoves( loudMoves,  board.isInLosingPos(!isEvaluationBlue), board.isInCheck(!isEvaluationBlue));
        for (Move move : loudMoves) {
            makeMove(move);
            switchColor();
            int score = quiescenceSearch(depth + 1 ,alpha, beta);
            switchColor();
            unmakeMove(move);
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


}
