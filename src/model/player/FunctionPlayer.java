package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;

public class FunctionPlayer extends Player{
    public FunctionPlayer(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction) {
        super(isBlue, board, moveGenerator, evaluationFunction);

    }

    @Override
    public Move decideMove() {
        Move decided = null;
        int score = Integer.MIN_VALUE;
        List<Move> moves = moveGenerator.generateMoves(isEvaluationBlue());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Move move : moves){
            makeMove(move);
            int evaluation = getEvaluationFunction().evaluate(isEvaluationBlue(),0);
            if (evaluation > score){
                decided = move;
                score = evaluation;
            }
            unmakeMove(move);
        }
        return decided;
    }
}
