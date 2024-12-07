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
            maximizer(currentSearchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            globalBest = best;
            currentSearchDepth++;
        }
        return globalBest;
    }

    private int maximizer(int depth, int alpha, int beta) {
        if (depth == 0)return evaluationFunction.evaluate(isEvaluationBlue,searchDepth);
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        if (currentSearchDepth > 1 && depth == searchDepth){
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
                if (depth == searchDepth)best = move;
            }
            if (alpha >= beta) return alpha;
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        if (depth == 0)return evaluationFunction.evaluate(isEvaluationBlue,searchDepth);
        moveNodes++;
        int minEval = Integer.MAX_VALUE;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int rating = maximizer(depth-1, alpha, beta);
            switchColor();
            unmakeMove(move);
            if (rating <= beta) beta = rating;
            if (alpha >= beta) return beta;
        }
        return minEval;
    }

}
