package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;

public class IterativeDeepeningFixedDepth extends Player{
    private final int searchDepth;
    private int currentSearchDepth = 1;
    private Move best;
    private Move globalBest;

    public IterativeDeepeningFixedDepth(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
    }

    @Override
    public Move decideMove() {
        while (currentSearchDepth < (searchDepth + 1)){
            iterateDepth(currentSearchDepth);
            currentSearchDepth++;
        }
        movesNodes.add(moveNodes);
        nodes += moveNodes;
        moveNodes = 0;
        currentSearchDepth = 1;
        return globalBest;
    }

    private int iterateDepth(int depth) {
        int depthScore = maximizer(depth,Integer.MIN_VALUE, Integer.MAX_VALUE);
        globalBest = best;
        return depthScore;
    }

    private int maximizer(int depth, int alpha, int beta) {
        if (depth == 0 || board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        if (currentSearchDepth > 1 && depth == currentSearchDepth){
            allMoves.remove(globalBest);
            allMoves.add(0,globalBest);
        }
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int rating = minimizer(depth-1, alpha, beta);
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
        if (depth == 0 || board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int rating = maximizer(depth-1, alpha, beta);
            switchColor();
            unmakeMove(move);
            if (rating < beta) beta = rating;
            if (alpha >= beta) return beta;
        }
        return beta;
    }

}
