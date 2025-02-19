package model.evolutionTheory.habitats;

import model.Board;
import model.evaluationFunction.EvaluationFunction;

public class SecondHabitatEvaluationFunction extends EvaluationFunction {
    public SecondHabitatEvaluationFunction (Board board){
        super(board, new int[]{2, 0, 1, 2, 2, 2, 6}, new int[]{2, 0, 1, 1}, new int[]{3, 20, 8, 2, 3, 5, 2}, new int[]{3, 7, 1, 1}, new int[]{10, 4, 1, -1, 2, 1, 15, 1}, new int[]{-489, -355, 0, -72, 0, 72, 355, 489}, 38, 38, 1);

    }
}
