package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;

public class MinMax extends Player{
    private final int searchDepth;
    private Move best;

    public MinMax(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
    }

    @Override
    public Move decideMove() {
        maximizer(searchDepth);
        //System.out.println("Best was " + best);
        //System.out.println(moveNodes);
        movesNodes.add(moveNodes);
        nodes += moveNodes;
        moveNodes = 0;
        return best;
    }

    private int maximizer(int depth) {
        if (depth == 0)return evaluationFunction.evaluate(isEvaluationBlue,searchDepth);
        moveNodes++;
        int maxEval = Integer.MIN_VALUE;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int eval = minimizer(depth - 1);
            switchColor();
            unmakeMove(move);
            if (eval > maxEval) {
                maxEval = eval;
                if (depth == searchDepth) best = move;
            }
        }
        return maxEval;
    }

    private int minimizer(int depth) {
        if (depth == 0)return evaluationFunction.evaluate(isEvaluationBlue,searchDepth);
        moveNodes++;
        int minEval = Integer.MAX_VALUE;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int eval = maximizer(depth - 1);
            switchColor();
            unmakeMove(move);
            if (eval < minEval) minEval = eval;
        }
        return minEval;
    }
}
