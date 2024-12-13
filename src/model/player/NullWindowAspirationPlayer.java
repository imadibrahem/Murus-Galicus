package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;

public class NullWindowAspirationPlayer extends Player{
    private final int searchDepth;
    private int currentSearchDepth = 1;
    private Move best;
    private Move globalBest;
    private final int window;
    private final int windowMultiplier;

    public NullWindowAspirationPlayer(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int window,int windowMultiplier, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        this.window = window;
        this.windowMultiplier = windowMultiplier;
    }

    public NullWindowAspirationPlayer(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        this.window = 10;
        this.windowMultiplier = 3;
    }

    @Override
    public Move decideMove() {
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
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        if (currentSearchDepth > 1 && depth == currentSearchDepth){
            allMoves.remove(globalBest);
            allMoves.add(0,globalBest);
        }
        List<Move> winningMoves = moveGenerator.generateWinningMoves(isEvaluationBlue());
        if (!moveGenerator.generateWinningMoves(isEvaluationBlue()).isEmpty()){
            allMoves.removeAll(winningMoves);
            allMoves.addAll(0, winningMoves);
        }
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
            }
            if (alpha >= beta) return alpha;
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        if (depth == 0) return quiescenceSearch(currentSearchDepth, alpha, beta);
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
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
            if (rating < beta) beta = rating;
            if (alpha >= beta) return beta;
        }
        return beta;
    }

    private int quiescenceSearch(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false) || (depth - currentSearchDepth) >= (currentSearchDepth))return evaluationFunction.evaluate(isEvaluationBlue,depth);
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
        List<Move> loudMoves = moveGenerator.generateLoudMoves(isBlue());
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
}
