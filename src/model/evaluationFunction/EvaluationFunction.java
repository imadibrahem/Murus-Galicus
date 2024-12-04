package model.evaluationFunction;

import model.Board;

public class EvaluationFunction {

    protected Board board;
    /*
    private final int distancesFactor;
    private final int columnsFactor;
    private final int towersFactor;
    private final int movesFactor;
    private final int gameStateFactor;
    private final int verticalChariotFactor;
    private final int horizontalChariotFactor;
    private final int diagonalChariotFactor;

     */

    public EvaluationFunction(Board board) {
        this.board = board;
    }

    public  int evaluate(boolean isBlue){
        return 0;
    }
}
