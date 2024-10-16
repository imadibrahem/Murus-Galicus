package model.evaluationFunction;

import model.Board;

public abstract class EvaluationFunction {

    protected Board board;

    public EvaluationFunction(Board board) {
        this.board = board;
    }

    public abstract int evaluate(boolean isBlue);
}
