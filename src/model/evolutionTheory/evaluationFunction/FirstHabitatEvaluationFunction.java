package model.evolutionTheory.evaluationFunction;

import model.Board;
import model.evaluationFunction.EvaluationFunction;

public class FirstHabitatEvaluationFunction extends EvaluationFunction {
    public FirstHabitatEvaluationFunction(Board board) {
        super(board, new int[]{0, 1, 1, 0, 1, 9, 2}, new int[]{13, 1, 1, -2}, new int[]{3, 0, 4, 5, 2, 1, 2}, new int[]{3, 1, 6, 13}, new int[]{7, 3, 5, 1, 3, 8, 0, 3}, new int[]{-204, -207, 0, -179, 0, 179, 207, 204}, 40, 44, 12);
    }
}
