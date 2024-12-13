package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;

public class AlphaBeta extends Player{
    private final int searchDepth;
    private Move best;

     public AlphaBeta(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
         super(isBlue, board, moveGenerator, evaluationFunction);
         this.searchDepth = searchDepth;
     }

    @Override
    public Move decideMove() {
        maximizer(searchDepth,Integer.MIN_VALUE, Integer.MAX_VALUE);
        movesNodes.add(moveNodes);
        nodes += moveNodes;
        moveNodes = 0;
        return best;
    }

    private int maximizer(int depth, int alpha, int beta) {
        if (depth == 0 || board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue,searchDepth - depth);
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        for (Move move : allMoves) {
            makeMove(move);
            switchColor();
            int rating = minimizer(depth-1, alpha, beta);
            switchColor();
            unmakeMove(move);
            if (rating > alpha){
                alpha = rating;
                if (depth == searchDepth) best = move;
            }
            if (alpha >= beta) return alpha;
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        if (depth == 0 || board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue,searchDepth - depth);
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
